"""
visualizations.py
Step 11: Creates six charts from the cleaned participation data.

It reuses retrieve_data() and clean_data() from participation_analysis.py,
so the charts use exactly the same cleaned data as Step 10.

Run it from the project folder:
    python python/visualizations.py

The six PNG charts are saved in the visualizations/ folder.
"""

import os

import matplotlib.pyplot as plt

from participation_analysis import retrieve_data, clean_data


# Folder where chart images are saved: <project>/visualizations
VIS_DIR = os.path.join(os.path.dirname(os.path.abspath(__file__)), "..", "visualizations")

# Order used for participation status (so the bars always appear in the same order)
PARTICIPATION_ORDER = ["Registered", "Participated", "Winner", "Runner-up"]


def save_chart(fig, filename):
    """Saves a figure as a PNG file in the visualizations folder."""
    os.makedirs(VIS_DIR, exist_ok=True)
    path = os.path.join(VIS_DIR, filename)
    fig.tight_layout()
    fig.savefig(path, dpi=150)
    plt.close(fig)
    print(f"Saved: {os.path.normpath(path)}")


# ------------------------------------------------------------
# Chart 1: Event-wise Registration Count (Bar Chart)
# ------------------------------------------------------------
def chart_event_registrations(df):
    # Count registrations per event, in date order
    counts = (
        df.groupby("event_name")
          .agg(total=("registration_id", "count"), event_date=("event_date", "min"))
          .sort_values("event_date")
    )

    fig, ax = plt.subplots(figsize=(10, 6))
    bars = ax.bar(counts.index, counts["total"], color="#2E5C8A")
    ax.bar_label(bars)
    ax.set_title("Event-wise Registration Count")
    ax.set_xlabel("Event Name")
    ax.set_ylabel("Number of Registrations")
    ax.tick_params(axis="x", rotation=20)
    save_chart(fig, "01_event_registrations.png")


# ------------------------------------------------------------
# Chart 2: Department-wise Student Participation (Bar Chart)
# ------------------------------------------------------------
def chart_department_participation(df):
    dept_counts = df.groupby("department").size().sort_values()

    fig, ax = plt.subplots(figsize=(10, 5))
    bars = ax.barh(dept_counts.index, dept_counts.values, color="#3C8D5A")
    ax.bar_label(bars)
    ax.set_title("Department-wise Student Participation")
    ax.set_xlabel("Number of Registrations")
    ax.set_ylabel("Department")
    save_chart(fig, "02_department_participation.png")


# ------------------------------------------------------------
# Chart 3: Attendance Status Distribution (Pie Chart)
# ------------------------------------------------------------
def chart_attendance_distribution(df):
    attendance_counts = df["attendance_status"].value_counts()

    fig, ax = plt.subplots(figsize=(7, 7))
    ax.pie(
        attendance_counts.values,
        labels=attendance_counts.index,
        autopct="%1.1f%%",
        startangle=90,
        colors=["#2E8B57", "#C0392B", "#95A5A6"][: len(attendance_counts)],
    )
    ax.set_title("Attendance Status Distribution")
    ax.axis("equal")  # keeps the pie circular
    save_chart(fig, "03_attendance_distribution.png")


# ------------------------------------------------------------
# Chart 4: Participation Status Analysis (Bar Chart)
# ------------------------------------------------------------
def chart_participation_status(df):
    status_counts = (
        df["participation_status"]
          .value_counts()
          .reindex(PARTICIPATION_ORDER, fill_value=0)
    )

    fig, ax = plt.subplots(figsize=(9, 6))
    bars = ax.bar(status_counts.index, status_counts.values, color="#E67E22")
    ax.bar_label(bars)
    ax.set_title("Participation Status Analysis")
    ax.set_xlabel("Participation Status")
    ax.set_ylabel("Number of Registrations")
    save_chart(fig, "04_participation_status.png")


# ------------------------------------------------------------
# Chart 5: Year-wise Participation (Bar Chart)
# ------------------------------------------------------------
def chart_year_participation(df):
    year_counts = df.groupby("year_of_study").size()
    labels = [f"Year {y}" for y in year_counts.index]

    fig, ax = plt.subplots(figsize=(8, 6))
    bars = ax.bar(labels, year_counts.values, color="#8E44AD")
    ax.bar_label(bars)
    ax.set_title("Year-wise Student Participation")
    ax.set_xlabel("Year of Study")
    ax.set_ylabel("Number of Registrations")
    save_chart(fig, "05_year_wise_participation.png")


# ------------------------------------------------------------
# Chart 6: Top Events Based on Student Registrations (Bar Chart)
# ------------------------------------------------------------
def chart_top_events(df, top_n=5):
    top = df.groupby("event_name").size().sort_values(ascending=False).head(top_n)

    # Reverse the order so the biggest bar is at the top of the chart
    fig, ax = plt.subplots(figsize=(10, 6))
    bars = ax.barh(top.index[::-1], top.values[::-1], color="#C0392B")
    ax.bar_label(bars)
    ax.set_title(f"Top {top_n} Events Based on Student Registrations")
    ax.set_xlabel("Number of Registrations")
    ax.set_ylabel("Event Name")
    save_chart(fig, "06_top_events.png")


# ------------------------------------------------------------
# Main program
# ------------------------------------------------------------
def main():
    print("=" * 62)
    print(" Smart Campus - Data Visualizations")
    print("=" * 62)

    print("\nRetrieving and cleaning data...")
    df_raw = retrieve_data()
    if df_raw.empty:
        print("No data found. Run schema.sql and sample_data.sql in MySQL first.")
        return
    df = clean_data(df_raw)
    print(f"Rows ready for charts: {len(df)}\n")

    print("Creating charts...")
    chart_event_registrations(df)
    chart_department_participation(df)
    chart_attendance_distribution(df)
    chart_participation_status(df)
    chart_year_participation(df)
    chart_top_events(df)

    print("\nAll six charts are saved in the visualizations/ folder.")


if __name__ == "__main__":
    main()
