from datetime import datetime
import requests
import pandas as pd
import json

url = "https://query.wikidata.org/sparql"

query = """
        SELECT ?videogame ?videogameLabel ?title ?genreLabel ?platformLabel ?publisherLabel ?publication_date ?developerLabel ?game_modeLabel ?minimum_age ?USK_ratingLabel ?PEGI_ratingLabel ?price
        WHERE {
            SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en". }
            ?videogame wdt:P31 wd:Q7889.
            ?videogame rdfs:label ?videogameLabel.
            FILTER(CONTAINS(LCASE(?videogameLabel), "%s"@en)).
            OPTIONAL { ?videogame wdt:P1476 ?title. }
            OPTIONAL { ?videogame wdt:P136 ?genre. }
            OPTIONAL { ?videogame wdt:P400 ?platform. }
            OPTIONAL { ?videogame wdt:P123 ?publisher. }
            OPTIONAL { ?videogame wdt:P577 ?publication_date. }
            OPTIONAL { ?videogame wdt:P178 ?developer. }
            OPTIONAL { ?videogame wdt:P404 ?game_mode. }
            OPTIONAL { ?videogame wdt:P2899 ?minimum_age. }
            OPTIONAL { ?videogame wdt:P914 ?USK_rating. }
            OPTIONAL { ?videogame wdt:P908 ?PEGI_rating. }
            OPTIONAL { ?videogame wdt:P2284 ?price. }
        }
        """


def make_query(title: str) -> str:
    return query % title.lower()


def get_wiki_result(title: str) -> pd.DataFrame:
    result = requests.get(url, params={"format": "json", "query": make_query(title)})
    # print(result.status_code)
    data = json.loads("{0}".format(result.text), strict=False)
    data = data["results"]["bindings"]
    data = [
        {attr_name: attr_dict["value"] for attr_name, attr_dict in game.items()}
        for game in data
    ]
    df = pd.DataFrame.from_dict(data).drop_duplicates()
    return df


if __name__ == "__main__":
    sales = pd.read_csv("data/vgsales.csv")
    titles = sales["Name"].unique().tolist()  # unique list of all titles
    titles_subset = titles[1::2]  # for testing get every second title
    print(len(titles_subset))
    df_full = pd.DataFrame()
    added_titles = 0
    for title in titles_subset:
        try:
            df = get_wiki_result(title)
        except ValueError as e:
            print("{0} -> {1}: {2}".format(title, type(e).__name__, e))
            continue
        print("{0} -> {1} -> {2}".format(title, len(df), df.columns.to_list()))
        if len(df) != 0:
            df_full = pd.concat([df_full, df])
            added_titles += 1
        if (
            added_titles == 15
        ):  # for testing make sure that 15 game requests are successful
            break
    df_full.drop_duplicates().reset_index(drop=True, inplace=True)
    print(df_full)
    df_full.to_csv("data/output_wikidata" + datetime.now().strftime("%d%m%Y-%H%M%S") + ".csv", index=False)
