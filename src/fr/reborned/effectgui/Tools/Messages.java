package fr.reborned.effectgui.Tools;

public class Messages {
    private String beforeMessage;
    private String hoverMessage;
    private String afterMessage;

    public Messages(String beforeMessage,String hoverMessage,String afterMessage){
        this.beforeMessage=beforeMessage;
        this.hoverMessage=hoverMessage;
        this.afterMessage=afterMessage;
    }

    public String getBeforeMessage() {
        return beforeMessage;
    }

    public void setBeforeMessage(String beforeMessage) {
        this.beforeMessage = beforeMessage;
    }

    public String getHoverMessage() {
        return hoverMessage;
    }

    public void setHoverMessage(String hoverMessage) {
        this.hoverMessage = hoverMessage;
    }

    public String getAfterMessage() {
        return afterMessage;
    }

    public void setAfterMessage(String afterMessage) {
        this.afterMessage = afterMessage;
    }
}
