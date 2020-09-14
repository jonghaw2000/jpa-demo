package com.exam.service;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.exam.entity.Singer;

@Service
@Repository
@Transactional
public class SingerServiceImpl implements SingerService {
	final static String ALL_SINGER_NATIVE_QUERY =
			"select id, first_name, last_name, birth_date, version from singer";
	
	@PersistenceContext
	private EntityManager em;

	@Transactional(readOnly = true)
	@Override
	public List<Singer> findAll() {
		return em.createNamedQuery(Singer.FIND_ALL, Singer.class).getResultList();
	}

	@Override
	public List<Singer> findAllWithAlbum() {
		List<Singer> singers = em.createNamedQuery(
				Singer.FIND_ALL_WITH_ALBUM, Singer.class).getResultList();
		return singers;
	}

	@Transactional(readOnly = true)
	@Override
	public Singer findById(Long id) {
		TypedQuery<Singer> query =
				em.createNamedQuery(Singer.FIND_SINGER_BY_ID, Singer.class);
		query.setParameter("id", id);
		return query.getSingleResult();
	}

	@Override
	public Singer save(Singer singer) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void delete(Singer singer) {
		// TODO Auto-generated method stub
		
	}

	@Transactional(readOnly = true)
	@Override
	public List<Singer> findAllByNativeQuery() {
		List<Singer> singers = em.createNamedQuery(
				Singer.FIND_ALL_WITH_ALBUM, Singer.class).getResultList();
		return singers;
	}
	
	
}
