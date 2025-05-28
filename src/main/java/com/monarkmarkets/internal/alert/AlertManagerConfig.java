package com.monarkmarkets.internal.alert;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlertManagerConfig {
    private final String url;
    private final String user;
    private final String password;
}
