import re

import pandas as pd

wikidata = pd.read_csv('data/output_wikidata_all.csv')
wikidata.info()

pegi_pattern = re.compile('PEGI (\d+)')
usk_pattern = re.compile('USK (\d+)')

for index, row in wikidata.iterrows():
    age = None
    # get pegi rating
    pegi_rating = None
    if not pd.isnull(row['PEGI_ratingLabel']):
        pegi_rating_label = row['PEGI_ratingLabel']
        match = pegi_pattern.match(pegi_rating_label)
        if match:
            pegi_rating = int(match.group(1))

    # get usk rating
    usk_rating = None
    if not pd.isnull(row['USK_ratingLabel']):
        usk_rating_label = row['USK_ratingLabel']
        match = usk_pattern.match(usk_rating_label)
        if match:
            usk_rating = int(match.group(1))

    if pegi_rating and usk_rating:
        age = pegi_rating if pegi_rating > usk_rating else usk_rating
    elif pegi_rating:
        age = pegi_rating
    elif usk_rating:
        age = usk_rating

    if age:
        wikidata.loc[index, 'minimum_age'] = str(age)

wikidata.to_csv('data/output_wikidata_all_age.csv')
