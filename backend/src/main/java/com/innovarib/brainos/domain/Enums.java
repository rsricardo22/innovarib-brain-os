package com.innovarib.brainos.domain;

public final class Enums {
    private Enums() {}
    public enum TenantStatus { ACTIVE, SUSPENDED, ARCHIVED }
    public enum UserStatus { INVITED, ACTIVE, DISABLED }
    public enum DocumentStatus { UPLOADED, PROCESSING, READY, FAILED, ARCHIVED }
    public enum LeadStatus { NEW, QUALIFIED, WON, LOST, ARCHIVED }
    public enum CampaignStatus { DRAFT, ACTIVE, PAUSED, COMPLETED, ARCHIVED }
    public enum EmailStatus { DRAFT, PENDING_APPROVAL, APPROVED, SENT, FAILED }
    public enum TaskStatus { TODO, IN_PROGRESS, BLOCKED, DONE, CANCELED }
    public enum AgentRunStatus { QUEUED, RUNNING, WAITING_APPROVAL, SUCCEEDED, FAILED, CANCELED }
    public enum ToolCallStatus { QUEUED, RUNNING, SUCCEEDED, FAILED, BLOCKED }
}
