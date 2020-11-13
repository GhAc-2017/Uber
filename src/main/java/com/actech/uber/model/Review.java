package com.actech.uber.model;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "review")
public class Review extends Auditable{
    private Integer ratingOutOfFive;
    private String note;
}
