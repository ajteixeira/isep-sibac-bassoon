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
  private final Map<String, Work> byName;

  public WorkCatalog() {
    this.works = KnowledgeBase.works();
    this.byName = works.stream().collect(Collectors.toMap(Work::getName, w -> w));
  }

  /** All works in the catalog. */
  public List<Work> all() {
    return works;
  }

  /** The work with this name, or null. */
  public Work byName(String name) {
    return byName.get(name);
  }

  /** The work with this id, or null. */
  public Work byId(double id) {
    return works.stream().filter(w -> w.getId() == id).findFirst().orElse(null);
  }
}
