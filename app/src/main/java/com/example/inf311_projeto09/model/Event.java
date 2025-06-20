package com.example.inf311_projeto09.model;

import java.util.Date;
import java.util.Map;

public record Event(int id,
                    String title,
                    String description,
                    String type,
                    String verificationCode,
                    int referEventId,
                    Date beginTime,
                    Date endTime,
                    Date checkInEnabled,
                    Date checkOutEnabled,
                    Date checkInTime,
                    Date checkOutTime,
                    EventStage eventStage) {

    public enum EventStage {
        CURRENT,
        NEXT,
        ENDED
    }

    public record RawEventResponse(int id, String processoNome, String etapaNome,
                                   Map<String, Object> camposPersonalizados) {
    }
}



