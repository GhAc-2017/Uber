package com.actech.uber.model;
import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "constant")
public class DBConstant extends Auditable{
    private String name;
    private String value;

    public Long getAsLong() {
        return Long.parseLong(value);
    }

    public String getValue(){
        return value;
    }
}
