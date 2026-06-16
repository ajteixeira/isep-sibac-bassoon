package org.sibac.bassoon.api.service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.sibac.bassoon.api.llm.JustificationService;
import org.sibac.bassoon.kb.KnowledgeBase;
import org.sibac.bassoon.model.Work;
import org.springframework.stereotype.Component;

/**
 * Shared catalog of works, loaded once at startup from {@link KnowledgeBase}.
 *
 * <p>Both {@link DroolsService} and {@link JustificationService} depend on this catalog
 * rather than on each other.
 */
@Component
public class WorkCatalog {

  private final List<Work> works;
  private final Map<Double, Work> byId;

  public WorkCatalog() {
    this.works = KnowledgeBase.works();
    this.byId = works.stream().collect(Collectors.toMap(Work::getId, w -> w));
  }

  public List<Work> all() {
    return works;
  }

  public Work byId(double id) {
    return byId.get(id);
  }
}
