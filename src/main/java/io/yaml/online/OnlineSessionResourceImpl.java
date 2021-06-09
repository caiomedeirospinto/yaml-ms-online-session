package io.yaml.online;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.ws.rs.InternalServerErrorException;

import io.yaml.online.beans.OnlineSession;
import io.yaml.online.repositories.OnlineSessionRepository;

public class OnlineSessionResourceImpl implements OnlineSessionResource {

  @Inject
  OnlineSessionRepository onlineSessionRepository;

  @Transactional
  @Override
  public OnlineSession createOnlineSession(OnlineSession data) {
    OnlineSession newSession = new OnlineSession();
    data.getProcesar().setOnlineSession(newSession);
    data.getProcesar().getCustomFields().stream().forEach(customField -> {
      customField.setProcesar(data.getProcesar());
    });
    if (data.getProcesar().getProgressField() != null) {
      data.getProcesar().getProgressField().setProcesar(data.getProcesar());
    }
    newSession.setProcesar(data.getProcesar());
    try {
      onlineSessionRepository.persistAndFlush(newSession);
      return newSession;
    } catch (Exception e) {
      e.printStackTrace();
      throw new InternalServerErrorException();
    }
  }

  @Override
  public OnlineSession getOnlineSession(Long sessionId) {
    return onlineSessionRepository.findById(sessionId);
  }
}