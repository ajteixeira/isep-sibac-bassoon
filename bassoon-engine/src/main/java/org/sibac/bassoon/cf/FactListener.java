package org.sibac.bassoon.cf;

import org.kie.api.event.rule.ObjectDeletedEvent;
import org.kie.api.event.rule.ObjectInsertedEvent;
import org.kie.api.event.rule.ObjectUpdatedEvent;
import org.kie.api.event.rule.RuleRuntimeEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Logs working-memory changes (inserts, updates, deletes) for debugging.
 * Added to the KieSession alongside {@link TrackingAgendaListener}.
 */
public class FactListener implements RuleRuntimeEventListener {

  private static final Logger LOG = LoggerFactory.getLogger(FactListener.class);

  @Override
  public void objectInserted(ObjectInsertedEvent event) {
    LOG.debug("Inserted: {}", event.getObject());
  }

  @Override
  public void objectUpdated(ObjectUpdatedEvent event) {
    LOG.debug("Updated: {}", event.getObject());
  }

  @Override
  public void objectDeleted(ObjectDeletedEvent event) {
    LOG.debug("Removed: {}", event.getOldObject());
  }
}
