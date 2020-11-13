package com.actech.uber.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.security.PrivateKey;
import java.util.Date;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class Auditable implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Override
    public int hashCode() {
        return id==0?0:id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this==obj) return true;
        if(!this.getClass().equals(obj.getClass())) return false;
        if(obj instanceof Auditable){
            Auditable auditable = (Auditable) obj;
            if( id==null && obj==null) return true;
            if(id==null || obj==null) return false;
            return id==((Auditable)obj).id;
        }
        else
            return super.equals(obj);
    }

    @Column(updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    @CreatedDate
    private Date createdAt;

    @Temporal(TemporalType.TIMESTAMP)
    @LastModifiedDate
    private Date updatedAt;
}
