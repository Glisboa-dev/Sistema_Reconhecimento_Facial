package org.glisboa.backend.utils.domain.repo;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

public class RepositoryUtils {

    public static <T> T findEntityByIdOrThrow(JpaRepository<T, Integer> repo, Integer id) {
        return repo.findById(id).orElseThrow(() -> new EntityNotFoundException("Dados não encontrados"));
    }

    public static <T> void deleteEntity(JpaRepository<T, Integer> repo, T entity){
        repo.delete(entity);
    }

    public static <T> void saveEntity(JpaRepository<T, Integer> repo, T entity){
        repo.save(entity);
    }
}
