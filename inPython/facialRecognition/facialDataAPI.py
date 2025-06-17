import os
import pandas as pd
import requests
import time

# API credentials
API_KEY = 'TconQGnE5dU0M_Y_b_2pHbHJ6duDToXp'
API_SECRET = 'wmdlNXRrVLlCnUupZuh3aADN3VYtM1dg'

# File paths
images_directory = r'C:\data in context\Extra Credit Sample\extra credit sample'
excel_file_path = r'C:\data in context\Extra Credit Labels.xlsx'

print(f"Using images from: {images_directory}")

# Load Excel file, skipping header stuff
df = pd.read_excel(excel_file_path, skiprows=3)

print(df.head())
def scores(api_age, validated_age_range, api_gender, validated_gender):
    val_age_str = str(validated_age_range).lower().strip()
    agescore = 0
    totalscore = 0
    if val_age_str == "more than 70":
        # Validated lower boundary is 70.
        lower = 70
        if api_age >= lower:
            agescore = 6
        else:
            diff = lower - api_age
            if diff >= 10:
                agescore = 0
            else:
                agescore = 6 * (1 - diff / 10) 
    else:
        try:
            lower, upper = map(int, validated_age_range.split('-'))
        except Exception as e:
            print(f"Error parsing validated age range '{validated_age_range}': {e}")
            return 0

        if lower <= api_age <= upper:
            return 6
        else:
        # Calculate distance from the nearest boundary.
            if api_age < lower:
                diff = lower - api_age
            else:
                diff = api_age - upper

            if diff >= 10:
                agescore = 0
            else:
                agescore = 6 * (1 - diff / 10)
    totalscore += agescore
    if api_gender.lower() == validated_gender.lower():
        totalscore += 4
    return totalscore

results = []
for idx, row in df.iterrows():
    image_filename = os.path.basename(row['file'])  # e.g., val/28.jpg -> 28.jpg
    image_path = os.path.join(images_directory, image_filename)

    if not os.path.exists(image_path):
        print(f"Image not found: {image_path}")
        continue

    with open(image_path, 'rb') as f:
        img_data = f.read()

    response = requests.post(
        'https://api-us.faceplusplus.com/facepp/v3/detect',
        data={
            'api_key': API_KEY,
            'api_secret': API_SECRET,
            'return_attributes': 'age,gender'
        },
        files={'image_file': img_data}
    )

    if response.status_code != 200:
        print(f"Error {response.status_code} for {image_filename}")
        continue

    data = response.json()
    if 'faces' in data and len(data['faces']) > 0:
        attributes = data['faces'][0]['attributes']
        age = attributes['age']['value']
        gender = attributes['gender']['value']

        validated_age = row['validated age']
        validated_gender = row['validated gender']
        validated_ethnicity = row['validated race/ethnic group']
        final_score  = scores(age, validated_age, gender, validated_gender)

        result = {
            'image_filename': image_filename,
            'predicted_age': age,
            'predicted_gender': gender,
            'validated_age': validated_age,
            'validated_gender': validated_gender,
            'validated_ethnicity': validated_ethnicity,
            'quality' : round(final_score,2)
        }
        results.append(result)
    else:
        print(f"{image_filename} -> No face detected")

    # Sleep to avoid rate limiting 
    time.sleep(1)
print(results)
results_df = pd.DataFrame(results)
total_score = results_df['quality'].mean()
print(total_score)
results_df.to_csv('facepp_results.csv', index=False)

