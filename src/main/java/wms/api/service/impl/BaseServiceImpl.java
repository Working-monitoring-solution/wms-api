package wms.api.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import wms.api.service.internal.TokenService;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public abstract class BaseServiceImpl<R extends JpaRepository<T, ID>, T, ID> {

    @Value("${spring.jwt.secretkey}")
    protected String secretkey;

    @Autowired
    protected TokenService tokenService;

    @Autowired
    protected R repo;

    protected Logger logger = LoggerFactory.getLogger(this.getClass());

    public Optional<T> save(T t) throws IllegalArgumentException {
        logger.info("\n\n ----> save record: {} \n\n", t.toString());
        t = repo.save(t);

        return Optional.of(t);
    }

    public <S extends T> Iterable<S> saveAll(Iterable<S> entities) {
        Iterable<S> sEntities = repo.saveAll(entities);
        return sEntities;
    }

    public Optional<T> findById(ID id) {
        return repo.findById(id);
    }

    public boolean existsById(ID id) {
        return repo.existsById(id);
    }

    public Iterable<T> findAll() {
        return repo.findAll();
    }

    public Iterable<T> findAllById(Iterable<ID> ids) {
        return repo.findAllById(ids);
    }

    public long count() {
        return repo.count();
    }

    public void deleteById(ID id) {
        repo.deleteById(id);
    }

    public void delete(T entity) {
        repo.delete(entity);
    }

    public void deleteAll(Iterable<? extends T> entities) {
        repo.deleteAll(entities);
    }

    public void deleteAll() {
        repo.deleteAll();
    }

    public Iterable<T> findAll(Sort sort) {
        return repo.findAll(sort);
    }

    public Page<T> findAll(Pageable pageable) {
        return repo.findAll(pageable);
    }

    protected String getTokenFromHeader(HttpServletRequest request) {
        return request.getHeader("WMS-Token");
    }
}
