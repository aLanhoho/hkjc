package com.win.big.model;

import com.fasterxml.jackson.databind.JsonNode;
import com.vladmihalcea.hibernate.type.json.JsonNodeBinaryType;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.time.LocalDateTime;

@TypeDef(
    name="jsonb-node",
    typeClass = JsonNodeBinaryType.class
)
@Entity(name="race_hist")
@Table(name="race_hist")
@Getter
@Setter
//@SequenceGenerator(initialValue = 1, allocationSize = 1, name="idgen", sequenceName = "race_hist_seq")
public class RaceHist {

    @EmbeddedId
    private RaceHistIdClass id;

    @Column(name="create_dt", nullable = false, updatable = false)
    private LocalDateTime createDateTime;

    @Column(name="update_dt", nullable = false)
    private LocalDateTime updateDateTime;

    @Type(type="jsonb-node")
    @Column(name="horse_rank", columnDefinition = "jsonb")
    private JsonNode horseRank;

    @Type(type="jsonb-node")
    @Column(name="horse_info", columnDefinition = "jsonb")
    private JsonNode horseInfo;

    @Type(type="jsonb-node")
    @Column(name="race_details", columnDefinition = "jsonb")
    private JsonNode raceDetails;

    @Type(type="jsonb-node")
    @Column(name="race_dividend", columnDefinition = "jsonb")
    private JsonNode raceDividend;
}
