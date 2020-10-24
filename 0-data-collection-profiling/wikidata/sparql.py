import json
from json import JSONDecodeError

import requests
import pandas as pd
from datetime import datetime

url = "https://query.wikidata.org/sparql"

query_grouped = """
select (?videogameLabel as ?title) (group_concat(DISTINCT ?genreLabel;SEPARATOR=";") as ?genres) (group_concat(DISTINCT ?platformLabel;SEPARATOR=";") as ?platforms) 
    (sample(?publisherLabel) as ?publisher) # ?publication_date ?price ?rating (sample(?game_modeLabel) as ?game_mode) ?USK_rating ?PEGI_rating
where {
  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en". }
  ?videogame wdt:P31 wd:Q7889.
  OPTIONAL { ?videogame wdt:P136 ?genreVar. }
  OPTIONAL { ?videogame wdt:P400 ?platformVar. }
  OPTIONAL { ?videogame wdt:P123 ?publisherVar. }
  OPTIONAL { ?videogame wdt:P577 ?publication_date. }
  OPTIONAL { ?videogame wdt:P2284 ?price. }
  OPTIONAL { ?videogame wdt:P444 ?rating. }
  OPTIONAL { ?videogame wdt:P404 ?game_modeVar. }
  OPTIONAL { ?videogame wdt:P914 ?USK_rating. }
  OPTIONAL { ?videogame wdt:P908 ?PEGI_rating. }
  
  SERVICE wikibase:label { 
    bd:serviceParam wikibase:language "en". 
    ?videogame rdfs:label ?videogameLabel.
    ?genreVar rdfs:label ?genreLabel.
    ?platformVar rdfs:label ?platformLabel.
    ?publisherVar rdfs:label ?publisherLabel.
    ?game_modeVar rdfs:label ?game_modeLabel.
  }
}
group by ?videogame ?videogameLabel

LIMIT 10
"""

query_count = """
SELECT (count(distinct ?videogame) as ?count)
WHERE {
  SERVICE wikibase:label { bd:serviceParam wikibase:language "[AUTO_LANGUAGE],en". }
  ?videogame wdt:P31 wd:Q7889.
}
"""

query_all = """
SELECT ?videogame ?videogameLabel ?title ?genreLabel ?platformLabel ?publisherLabel ?publication_date ?price ?rating ?game_modeLabel ?USK_ratingLabel ?PEGI_ratingLabel WHERE {
  #SERVICE wikibase:label { bd:serviceParam wikibase:language '[AUTO_LANGUAGE],en'. }
  ?videogame wdt:P31 wd:Q7889.
  OPTIONAL { ?videogame wdt:P1476 ?title. }
  OPTIONAL { ?videogame wdt:P136 ?genreVar. }
  OPTIONAL { ?videogame wdt:P400 ?platformVar. }
  OPTIONAL { ?videogame wdt:P123 ?publisher. }
  OPTIONAL { ?videogame wdt:P577 ?publication_date. }
  OPTIONAL { ?videogame wdt:P2284 ?price. }
  OPTIONAL { ?videogame wdt:P444 ?rating. }
  OPTIONAL { ?videogame wdt:P404 ?game_mode. }
  OPTIONAL { ?videogame wdt:P914 ?USK_rating. }
  OPTIONAL { ?videogame wdt:P908 ?PEGI_rating. }
  
  SERVICE wikibase:label { 
    bd:serviceParam wikibase:language 'en'. 
    ?videogame rdfs:label ?videogameLabel.
    ?genreVar rdfs:label ?genreLabel.
    ?platformVar rdfs:label ?platformLabel.
    #?publisherVar rdfs:label ?publisherLabel.
    #?game_modeVar rdfs:label ?game_modeLabel.
  }
  
  filter( regex(str(?videogameLabel), '^[Aa]' ))
}
ORDER BY ?videogameLabel
LIMIT 10
OFFSET 0

"""

result = requests.get(url, params={'format': 'json', 'query': query_all})
print(len(result.text))


def remove_nulls(d):
    return {k: d[k] for k in d if d[k] is not None}

# or r"{}".format(string)
result_text = result.text.replace('"[AUTO_LANGUAGE],en"', "'[AUTO_LANGUAGE],en'")
print(result_text)
try:
    data = json.loads(r"{}".format(result_text), strict=False, object_hook=remove_nulls)
except JSONDecodeError as e:
    print('ERROR')
    print(e.msg)
    print(result_text[e.pos])
    print(result_text[e.pos - 50: e.pos + 50])

data = data['results']['bindings']

data = [{attr_name: attr_dict['value'] for attr_name, attr_dict in game.items()}
        for game in data]

df = pd.DataFrame.from_dict(data)
df.head(5)
df.info()

df.to_excel('data/output_' + datetime.now().strftime("%d%m%Y-%H%M%S") + '.xlsx',
            engine='openpyxl',
            index=False)
