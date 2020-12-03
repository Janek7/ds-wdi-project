/*
 * Copyright (c) 2017 Data and Web Science Group, University of Mannheim, Germany (http://dws.informatik.uni-mannheim.de/)
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and limitations under the License.
 */
package de.uni_mannheim.informatik.dws.wdi.ExerciseDataFusion.model_new;

import de.uni_mannheim.informatik.dws.winter.model.AbstractRecord;
import de.uni_mannheim.informatik.dws.winter.model.defaultmodel.Attribute;
import org.apache.commons.lang3.StringUtils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.*;


public class VideoGame extends AbstractRecord<Attribute> implements Serializable {

	private static final long serialVersionUID = 1L;
    public static final Attribute TITLE = new Attribute("Title");
    public static final Attribute PLATFORM = new Attribute("Platform");
    public static final Attribute PUBLISHER = new Attribute("Publisher");
    public static final Attribute PUBLISHING_DATE = new Attribute("PublishingDate");
    public static final Attribute DEVELOPER = new Attribute("Developer");
    public static final Attribute GENRES = new Attribute("Genres");
    public static final Attribute GAME_MODES = new Attribute("GameModes");
    public static final Attribute PRICE = new Attribute("price");
    public static final Attribute AGE = new Attribute("Age");
    public static final Attribute USK_RATING = new Attribute("UskRating");
    public static final Attribute PEGI_RATING = new Attribute("PegiRating");

	private VideoGameDataSource source;
	protected String id;
	private String title;
	private String platform;
	private String publisher;
	private LocalDate publishingdate;
	private List<Genre> genres;
	private List<GameMode> gameModes;
	private String developer;
	private Integer age;
	private Double price;
	private String uskRating;
	private String pegiRating;

	private boolean ageWasRead = false;
	private boolean priceWasRead = false;

    private Map<Attribute, Collection<String>> provenance = new HashMap<>();
    private Collection<String> recordProvenance;

	public VideoGame(String identifier, String provenance) {

        super(identifier, provenance);
        this.id = identifier;
        this.genres = new LinkedList<>();
        this.gameModes = new LinkedList<>();

		if (id.contains("wikidata")) {
			this.source = VideoGameDataSource.WIKIDATA;
		} else if (id.contains("vg_sales")) {
			this.source = VideoGameDataSource.SALES;
		} else if (id.contains("steam_games")) {
			this.source = VideoGameDataSource.STEAM;
		}

	}

	// METHODS

	@Override
	public boolean hasValue(Attribute attribute) {

        if(attribute == TITLE)
            return getTitle() != null && !getTitle().isEmpty();
        else if(attribute == PLATFORM)
            return getPlatform() != null && !getPlatform().isEmpty();
        else if(attribute == PUBLISHER)
            return getPublisher() != null && !getPublisher().isEmpty();
        else if(attribute == PUBLISHING_DATE)
            return getPublishingDate() != null;
        else if(attribute == DEVELOPER)
            return getDeveloper() != null && !getDeveloper().isEmpty();
        else if(attribute == GENRES)
            return getGenres() != null && getGenres().size() > 0;
        else if(attribute == GAME_MODES)
            return getGameModes() != null && getGameModes().size() > 0;
        else if(attribute == PRICE)
            return getPrice() != null;
        else if(attribute == AGE)
            return getAge() != null;
        else if(attribute == USK_RATING)
            return getUskRating() != null && !getUskRating().isEmpty();
        else if(attribute == PEGI_RATING)
            return getPegiRating() != null && !getPegiRating().isEmpty();
        else
            return false;

	}

    @Override
    public String toString(){
        /*return String.format("[VideoGame %s: %s / %s / %s / %s / %s / %s]\n", getIdentifier(), getTitle(),
                getPlatform(), getPublishingDate().toString(), getGenres().toString(), getUskRating(), getPegiRating());*/
    	return getIdentifier();
    }

    @Override
    public int hashCode() {
        return getIdentifier().hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof VideoGame) {
            return this.getIdentifier().equals(((VideoGame) obj).getIdentifier());
        } else
            return false;
    }


	// GETTER AND SETTERS

    // PROVENANCE ATTRIBUTES

    public Collection<String> getRecordProvenance() {
        return recordProvenance;
    }
    public Collection<String> getAttributeProvenance(String attribute) {
        return provenance.get(attribute);
    }

    public void setRecordProvenance(Collection<String> provenance) {
        recordProvenance = provenance;
    }

    public void setAttributeProvenance(Attribute attribute,
                                       Collection<String> provenance) {
        this.provenance.put(attribute, provenance);
    }

    public String getMergedAttributeProvenance(Attribute attribute) {
        Collection<String> prov = provenance.get(attribute);

        if (prov != null) {
            return StringUtils.join(prov, "+");
        } else {
            return "";
        }
    }

    // VIDEOGAME ATTRIBUTES

    @Override
    public String getIdentifier() {
        return id;
    }

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getPlatform() {
		return platform;
	}

	public void setPlatform(String platform) {
		this.platform = platform;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public LocalDate getPublishingDate() {
		return publishingdate;
	}

	public void setPublishingDate(LocalDate publishingDate) {
		this.publishingdate = publishingDate;
	}

	public List<Genre> getGenres() {
		return genres;
	}

	public void setGenres(List<Genre> genres) {
		this.genres = genres;
	}

	public List<GameMode> getGameModes() {
		return gameModes;
	}

	public void setGameModes(List<GameMode> gameModes) {
		this.gameModes = gameModes;
	}

	public String getDeveloper() {
		return developer;
	}

	public void setDeveloper(String developer) {
		this.developer = developer;
	}

	public Integer getAge() {
		return age;
	}

	public void setAge(Integer age) {
		this.age = age;
	}

	public Double getPrice() {
		return price;
	}

	public void setPrice(Double price) {
		this.price = price;
	}

	public VideoGameDataSource getSource() {
		return source;
	}

	public String getUskRating() {
		return uskRating;
	}

	public void setUskRating(String uskRating) {
		this.uskRating = uskRating;
	}

	public String getPegiRating() {
		return pegiRating;
	}

	public void setPegiRating(String pegiRating) {
		this.pegiRating = pegiRating;
	}


	public boolean isAgeWasRead() {
		return ageWasRead;
	}

	public void setAgeWasRead(boolean ageWasRead) {
		this.ageWasRead = ageWasRead;
	}

	public boolean isPriceWasRead() {
		return priceWasRead;
	}

	public void setPriceWasRead(boolean priceWasRead) {
		this.priceWasRead = priceWasRead;
	}
}
