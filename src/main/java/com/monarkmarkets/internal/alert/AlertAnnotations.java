package com.monarkmarkets.internal.alert;

import lombok.Data;

@Data
public class AlertAnnotations {
    private String title;
    private String message;
    private String details;
    private String action;
    private String linkUrl;
    private String linkText;
}
