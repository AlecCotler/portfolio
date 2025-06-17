import pandas as pd
from scipy.stats import ttest_ind

# Load the CSV file (skip first 4 non-data rows)
df = pd.read_csv("facepp_results.csv", skiprows=4)

# Convert 'quality' to numeric and drop rows with missing quality
df["quality"] = pd.to_numeric(df["quality"], errors="coerce")
df = df.dropna(subset=["quality"])

# Function to compute summary stats and p-values
def group_summary(df, group_cols):
    summaries = []

    # Group by the specified columns
    grouped = df.groupby(group_cols)

    for group_vals, group_df in grouped:
        group = group_df["quality"]

        # Compute summary stats
        summary = {
            "group": group_vals if isinstance(group_vals, tuple) else (group_vals,),
            "count": len(group),
            "mean": group.mean(),
            "median": group.median(),
            "min": group.min(),
            "max": group.max(),
            "std_dev": group.std(),
            "p_value": ttest_ind(group, df["quality"], equal_var=False).pvalue
        }
        summaries.append(summary)

    # Create DataFrame and unpack group columns
    summary_df = pd.DataFrame(summaries)
    if summary_df.empty:
        return summary_df

    if isinstance(group_cols, list):
        for i, col in enumerate(group_cols):
            summary_df[col] = summary_df["group"].apply(lambda x: x[i])
    else:
        summary_df[group_cols] = summary_df["group"].apply(lambda x: x[0])

    summary_df = summary_df.drop(columns=["group"])

    # Drop NaNs and sort by p-value
    return summary_df.dropna(subset=["p_value"]).sort_values(by="p_value")

# Define all groupings and output names
groupings = [
    (["validated_age"], "age"),
    (["validated_gender"], "gender"),
    (["validated_ethnicity"], "ethnicity"),
    (["validated_age", "validated_gender"], "age_gender"),
    (["validated_age", "validated_ethnicity"], "age_ethnicity"),
    (["validated_ethnicity", "validated_gender"], "ethnicity_gender"),
]

# Process and save each grouping
for cols, name in groupings:
    print(f"🔹 Quality Rating Summary by {name.replace('_', ' ').title()}:\n")
    stats = group_summary(df, cols)
    if not stats.empty:
        print(stats, "\n")
        stats.to_csv(f"{name}_quality_stats.csv", index=False)
    else:
        print(f"No valid data for {name} grouping.\n")
