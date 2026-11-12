"""
participation_analysis.py
Step 10: Retrieve, check, clean, and summarize student participation data.

What this program does:
  1. Connects to the MySQL database (smart_campus_db).
  2. Retrieves registration data with one JOIN query.
  3. Loads the data into a pandas DataFrame.
  4. Checks for missing values and duplicate records.
  5. Cleans the data and converts types.
  6. Prints summary statistics.
  7. Saves the cleaned data and the summary to the outputs/ folder.

Run it from the project folder:
    python python/participation_analysis.py
"""

import io
import os
import sys
from contextlib import redirect_stdout

import mysql.connector
import pandas as pd

from db_config import DB_CONFIG


# Folder where results are saved: <project>/outputs
OUTPUT_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "outputs")

# One query that joins all three tables.
# Each row = one registration, with the student and event details.
PARTICIPATION_QUERY = """
SELECT r.registration_id,
       r.student_id,
       s.student_name,
       s.department,
       s.year_of_study,
       r.event_id,
       e.event_name,
       e.event_date,
       e.venue,
       r.registration_date,
       r.attendance_status,
       r.participation_status
FROM REGISTRATION r
INNER JOIN STUDENT s ON r.student_id = s.student_id
INNER JOIN EVENT e   ON r.event_id = e.event_id
ORDER BY r.registration_id
"""

# Allowed values (must match the CHECK rules in schema.sql)
VALID_ATTENDANCE = ["Present", "Absent"]
VALID_PARTICIPATION = ["Registered", "Participated", "Winner", "Runner-up"]


# ------------------------------------------------------------
# Step A: Retrieve data from MySQL
# ------------------------------------------------------------
def get_connection():
    """Opens a connection to MySQL using the settings in db_config.py."""
    return mysql.connector.connect(**DB_CONFIG)


def retrieve_data():
    """Runs the JOIN query and returns the result as a pandas DataFrame."""
    conn = None
    try:
        conn = get_connection()
        cursor = conn.cursor()
        cursor.execute(PARTICIPATION_QUERY)
        rows = cursor.fetchall()
        columns = [col[0] for col in cursor.description]
        cursor.close()
    except mysql.connector.Error as err:
        print(f"Database error: {err}")
        print("Check that MySQL is running and that db_config.py has the right password.")
        sys.exit(1)
    finally:
        if conn is not None and conn.is_connected():
            conn.close()

    return pd.DataFrame(rows, columns=columns)


# ------------------------------------------------------------
# Step B: Check the data (missing values and duplicates)
# ------------------------------------------------------------
def check_missing_values(df):
    """Prints how many values are missing in each column."""
    missing = df.isna().sum()
    print(missing.to_string())
    if missing.sum() == 0:
        print("No missing values found.")
    else:
        print(f"Total missing values: {missing.sum()}")
    return missing


def check_duplicates(df):
    """Checks for duplicate rows, duplicate IDs, and duplicate student-event pairs."""
    full_duplicates = df.duplicated().sum()
    id_duplicates = df.duplicated(subset=["registration_id"]).sum()
    pair_duplicates = df.duplicated(subset=["student_id", "event_id"]).sum()

    print(f"Fully duplicated rows:           {full_duplicates}")
    print(f"Duplicate registration IDs:      {id_duplicates}")
    print(f"Duplicate student-event pairs:   {pair_duplicates}")
    return full_duplicates, id_duplicates, pair_duplicates


# ------------------------------------------------------------
# Step C: Clean and convert the data
# ------------------------------------------------------------
def clean_data(df):
    """Cleans the DataFrame and returns a new, cleaned copy."""
    df = df.copy()

    # 1. Remove extra spaces from text columns and use pandas "string" type
    text_columns = [
        "student_id", "student_name", "department",
        "event_name", "venue", "attendance_status", "participation_status",
    ]
    for col in text_columns:
        df[col] = df[col].astype("string").str.strip()

    # 2. Remove duplicate registrations (keep the first one)
    df = df.drop_duplicates(subset=["registration_id"], keep="first")

    # 3. Drop rows that are missing the IDs (they cannot be used at all)
    df = df.dropna(subset=["registration_id", "student_id", "event_id"])

    # 4. Fill missing descriptive text with "Unknown"
    for col in ["student_name", "department", "event_name", "venue"]:
        df[col] = df[col].fillna("Unknown")

    # 5. Mark invalid status values as "Unknown"
    df["attendance_status"] = df["attendance_status"].fillna("Unknown")
    df["participation_status"] = df["participation_status"].fillna("Unknown")
    df.loc[~df["attendance_status"].isin(VALID_ATTENDANCE), "attendance_status"] = "Unknown"
    df.loc[~df["participation_status"].isin(VALID_PARTICIPATION), "participation_status"] = "Unknown"

    # 6. Convert types
    df["registration_id"] = df["registration_id"].astype(int)
    df["event_id"] = df["event_id"].astype(int)
    df["year_of_study"] = pd.to_numeric(df["year_of_study"], errors="coerce").astype("Int64")
    df["event_date"] = pd.to_datetime(df["event_date"], errors="coerce")
    df["registration_date"] = pd.to_datetime(df["registration_date"], errors="coerce")

    # 7. Add helpful columns for analysis
    df["attended"] = (df["attendance_status"] == "Present").astype(int)
    df["is_prize_winner"] = df["participation_status"].isin(["Winner", "Runner-up"]).astype(int)
    df["event_month"] = df["event_date"].dt.strftime("%Y-%m")

    return df.reset_index(drop=True)


# ------------------------------------------------------------
# Step D: Summary statistics
# ------------------------------------------------------------
def summary_statistics(df):
    """Prints the main summary numbers for the cleaned data."""
    if df.empty:
        print("No data to summarize.")
        return

    print(f"Total registrations:  {len(df)}")
    print(f"Unique students:      {df['student_id'].nunique()}")
    print(f"Unique events:        {df['event_id'].nunique()}")
    print(f"Attendance rate:      {df['attended'].mean() * 100:.1f}%")
    print(f"Prize winners (Winner or Runner-up): {df['is_prize_winner'].sum()}")

    print("\nAttendance status counts:")
    print(df["attendance_status"].value_counts().to_string())

    print("\nParticipation status counts:")
    print(df["participation_status"].value_counts().to_string())

    print("\nRegistrations per event:")
    print(df.groupby("event_name").size().sort_values(ascending=False).to_string())

    print("\nRegistrations per department:")
    print(df.groupby("department").size().sort_values(ascending=False).to_string())

    print("\nRegistrations per year of study:")
    print(df.groupby("year_of_study").size().to_string())

    print("\nEvents per student (descriptive statistics):")
    print(df.groupby("student_id").size().describe().round(2).to_string())

    print("\nNumeric columns (descriptive statistics):")
    print(df[["year_of_study", "attended", "is_prize_winner"]].describe().round(2).to_string())


# ------------------------------------------------------------
# Step E: Save results
# ------------------------------------------------------------
def save_results(df, summary_text):
    """Saves the cleaned data (CSV) and the summary (TXT) into the outputs folder."""
    os.makedirs(OUTPUT_DIR, exist_ok=True)

    csv_path = os.path.join(OUTPUT_DIR, "cleaned_participation_data.csv")
    df.to_csv(csv_path, index=False)

    txt_path = os.path.join(OUTPUT_DIR, "python_summary.txt")
    with open(txt_path, "w", encoding="utf-8") as f:
        f.write(summary_text)

    print(f"Saved cleaned data to: {os.path.normpath(csv_path)}")
    print(f"Saved summary to:      {os.path.normpath(txt_path)}")


# ------------------------------------------------------------
# Main program
# ------------------------------------------------------------
def main():
    print("=" * 62)
    print(" Smart Campus - Participation Data Retrieval and Preprocessing")
    print("=" * 62)

    print("\n[1] Retrieving data from MySQL...")
    df_raw = retrieve_data()
    print(f"Rows retrieved: {len(df_raw)}")

    if df_raw.empty:
        print("No data found. Run schema.sql and sample_data.sql in MySQL first.")
        return

    print("\n[2] Raw data preview:")
    print(df_raw.head().to_string())

    print("\n[3] Checking missing values:")
    check_missing_values(df_raw)

    print("\n[4] Checking duplicate records:")
    check_duplicates(df_raw)

    print("\n[5] Cleaning data...")
    df_clean = clean_data(df_raw)
    print(f"Rows after cleaning: {len(df_clean)}")

    print("\n[6] Column types after conversion:")
    print(df_clean.dtypes.to_string())

    print("\n[7] Summary statistics:")
    buffer = io.StringIO()
    with redirect_stdout(buffer):
        summary_statistics(df_clean)
    summary_text = buffer.getvalue()
    print(summary_text)

    print("[8] Saving results...")
    save_results(df_clean, summary_text)

    print("\nDone. Next step: Step 11 (visualizations).")


if __name__ == "__main__":
    main()
