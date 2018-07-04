package com.tama.q_municate_db.models;

import static com.tama.q_municate_db.models.Message.Column.BODY;
import static com.tama.q_municate_db.models.Message.Column.CREATED_DATE;
import static com.tama.q_municate_db.models.Message.Column.ID;
import static com.tama.q_municate_db.models.Message.Column.SNAP_MESSAGE;
import static com.tama.q_municate_db.models.Message.Column.IS_SNAP_IN_PROGRESS;
import static com.tama.q_municate_db.models.Message.Column.SNAP_MESSAGE_TIME;
import static com.tama.q_municate_db.models.Message.Column.STATE;
import static com.tama.q_municate_db.models.Message.Column.TABLE_NAME;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import java.io.Serializable;

@DatabaseTable(tableName = TABLE_NAME)
public class Message implements Serializable {

    @DatabaseField(
            id = true,
            unique = true,
            columnName = ID)
    private String messageId;

    @DatabaseField(
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = false,
            columnDefinition = "INTEGER REFERENCES " + DialogOccupant.Column.TABLE_NAME + "(" + DialogOccupant.Column.ID + ") ON DELETE CASCADE",
            columnName = DialogOccupant.Column.ID)
    private DialogOccupant dialogOccupant;

    @DatabaseField(
            foreign = true,
            foreignAutoRefresh = true,
            canBeNull = true,
            columnName = Attachment.Column.ID)
    private Attachment attachment;

    @DatabaseField(
            columnName = STATE)
    private State state;

    @DatabaseField(
            columnName = BODY)
    private String body;

    @DatabaseField(
        columnName = SNAP_MESSAGE)
    private Boolean snapMessage;


    @DatabaseField(
            columnName = IS_SNAP_IN_PROGRESS)
    private Boolean isSnapInProgress;


    @DatabaseField(
            columnName = SNAP_MESSAGE_TIME)
    private long snapMessageTime;

//    @DatabaseField(
//            dataType = DataType.SERIALIZABLE,
//            columnName = TIMER_TASK)
//    private AsyncTask timerTask;

    @DatabaseField(
            columnName = CREATED_DATE)
    private long createdDate;

    public Message() {
    }

    public Message(String messageId, DialogOccupant dialogOccupant, Attachment attachment, State state,
            String body, Boolean bool, long time, Boolean isSnapInProgress, long createdDate) {
        this.messageId = messageId;
        this.dialogOccupant = dialogOccupant;
        this.attachment = attachment;
        this.state = state;
        this.body = body;
        this.createdDate = createdDate;
        this.snapMessage = bool;
        this.isSnapInProgress = isSnapInProgress;
        this.snapMessageTime = time;
//        this.timerTask = asyncTask;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public long getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(long createdDate) {
        this.createdDate = createdDate;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public void setSnapMessageTime(long time){
        this.snapMessageTime = time;
    }

    public long getSnapMessageTime(){
        return snapMessageTime;
    }

    public void setSnapMessage(Boolean bool){
        this.snapMessage = bool;
    }

    public Boolean getSnapMessage(){
        return snapMessage ==null ? false: snapMessage;
    }

    public void setIsSnapInProgress(Boolean bool){
        isSnapInProgress = bool;
    }

    public Boolean getIsSnapInProgress(){
        return isSnapInProgress==null ? false:isSnapInProgress;
    }

//    public void setTimerTask(AsyncTask asyncTask){
//        this.timerTask = asyncTask;
//    }
//
//    public AsyncTask getTimerTask(){
//        return (AsyncTask)timerTask;
//    }

    public Attachment getAttachment() {
        return attachment;
    }

    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    public State getState() {
        return state;
    }

    public void setState(State state) {
        this.state = state;
    }

    public DialogOccupant getDialogOccupant() {
        return dialogOccupant;
    }

    public void setDialogOccupant(DialogOccupant dialogOccupant) {
        this.dialogOccupant = dialogOccupant;
    }

    public boolean isIncoming(int currentUserId) {
        return dialogOccupant != null && currentUserId != dialogOccupant.getUser().getId();
    }

    @Override
    public String toString() {
        return "Message [messageId='" + messageId
                + "', dialogOccupant='" + dialogOccupant
                + "', body='" + body
                + "', snapMessage='" + snapMessage
                + "', isSnapInProgress='" + isSnapInProgress
                + "', snapMessageTime='" + snapMessageTime
//                + "', timerTask='" + timerTask
                + "', createdDate='" + createdDate + "']";
    }

    public interface Column {

        String TABLE_NAME = "message";
        String ID = "message_id";
        String BODY = "body";
        String STATE = "state";
        String SNAP_MESSAGE = "snap_message";
        String IS_SNAP_IN_PROGRESS = "is_snap_in_progress";
        String SNAP_MESSAGE_TIME = "snap_message_time";
        String CREATED_DATE = "created_date";
        String TIMER_TASK = "timer_task";

    }
}