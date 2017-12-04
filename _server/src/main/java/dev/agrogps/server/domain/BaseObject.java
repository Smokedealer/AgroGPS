package dev.agrogps.server.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import java.io.Serializable;


@MappedSuperclass
public class BaseObject implements Serializable  {

    /**
     * PK
     */
    protected Long id;


    /**
     *
     * @return true if the entity hasn't been persisted yet
     */
    @Transient
    @JsonIgnore
    public boolean isNew() {
        return this.id == null;
    }

    @Id
    @GeneratedValue
    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || this.getClass() != o.getClass()) return false;

        BaseObject that = (BaseObject) o;

        return this.id != null ? this.id.equals(that.id) : that.id == null;
    }


    @Override
    public int hashCode() {
        return this.id != null ? this.id.hashCode() : 0;
    }



}
