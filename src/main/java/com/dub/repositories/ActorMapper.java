package com.dub.repositories;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.springframework.jdbc.core.RowMapper;

import com.dub.entities.Actor;

public class ActorMapper implements RowMapper<Actor>{
	
	public Actor mapRow(ResultSet rs, int rowNum) throws SQLException {

		Actor actor = new Actor();

		actor.setId(rs.getInt("id"));
		actor.setFirstName(rs.getString("firstname"));
		actor.setLastName(rs.getString("lastname"));
		actor.setBirthDate(rs.getDate("birthdate"));
		
		return actor;
   }// mapRow
}
