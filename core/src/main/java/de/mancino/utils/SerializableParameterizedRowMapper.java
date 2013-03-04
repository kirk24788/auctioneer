package de.mancino.utils;

import java.io.Serializable;

import org.springframework.jdbc.core.simple.ParameterizedRowMapper;


public interface SerializableParameterizedRowMapper<T> extends ParameterizedRowMapper<T>, Serializable  {

}
