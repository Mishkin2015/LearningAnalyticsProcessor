/**
 * 
 */
package org.apereo.lap.services.storage.mongo;

import java.util.Collection;
import java.util.List;

import org.apereo.lap.services.storage.ModelOutput;
import org.apereo.lap.services.storage.PersistentStorage;
import org.apereo.lap.services.storage.mongo.model.MongoModelOutputRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

/**
 * @author ggilbert
 *
 */
@Component("MongoDB")
@Profile({"mongo", "mongo-multitenant"})
public class MongoPersistentStorage implements PersistentStorage<ModelOutput> {
  
  @Autowired
  private MongoModelOutputRepository mongoModelOutputRepository;

  @Override
  public ModelOutput save(ModelOutput persistentLAPEntity) {
    return mongoModelOutputRepository.save((ModelOutput)persistentLAPEntity);
  }

  @Override
  public List<ModelOutput> saveAll(Collection<ModelOutput> persistentLAPentities) {
    List<ModelOutput> mo = mongoModelOutputRepository.save(persistentLAPentities);
    return mo;
  }

  @Override
  public Page<ModelOutput> findAll(Pageable pageable) {
    Page<ModelOutput> mo = mongoModelOutputRepository.findAll(pageable);
    return mo;
  }

  @Override
  public Page<ModelOutput> findByStudentId(String studentId, Pageable pageable) {
    return mongoModelOutputRepository.findByStudentId(studentId, pageable);
  }

  @Override
  public Page<ModelOutput> findByCourseId(String courseId, boolean onlyLastRun, Pageable pageable) {
    Page<ModelOutput> page = null;
    if (onlyLastRun) {
      ModelOutput modelOutput = mongoModelOutputRepository.findTopByCourseIdOrderByCreatedDateDesc(courseId);
      if (modelOutput != null) {
        page = mongoModelOutputRepository.findByModelRunIdAndCourseId(modelOutput.getModelRunId(), courseId, pageable);
      }
    }
    else {
      page = mongoModelOutputRepository.findByCourseId(courseId, pageable);
    }
    
    return page;
  }

  @Override
  public Page<ModelOutput> findByStudentIdAndCourseId(String studentId, String courseId, boolean onlyLastRun, Pageable pageable) {
    Page<ModelOutput> page = null;
    if (onlyLastRun) {
      ModelOutput modelOutput = mongoModelOutputRepository.findTopByCourseIdOrderByCreatedDateDesc(courseId);
      if (modelOutput != null) {
        page = mongoModelOutputRepository.findTopByCourseIdAndStudentIdOrderByCreatedDateDesc(courseId, studentId, pageable);
      }
    }
    else {
      page = mongoModelOutputRepository.findByStudentIdAndCourseId(studentId, courseId, pageable);
    }
    
    return page;
  }

}