package org.esupportail.lecture.dao;

import java.util.List;

import org.esupportail.lecture.domain.model.TestENUM;

public interface DaoTestENUM {

	public abstract TestENUM getTestENUM(String enumpk);

	public abstract void saveTestENUM(TestENUM testENUM);

	public abstract void deleteTestENUM(TestENUM testENUM);

	public List<TestENUM> getTestENUMs();

}