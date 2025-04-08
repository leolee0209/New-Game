package Main;

public class Button {

    public static enum Type {
        Default,
        Plus,
        Minus
    }
    
    public Type buttonType;
    public int x, y;
    public int width, height;
    public String content;
    public boolean enabled = false;
    public String linkedContent;

    public Button(int x, int y, int width, int height, String content) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
        this.buttonType = Type.Default;
    }
    public Button(int x, int y, int width, int height, String content, Type type, String linkedContent) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.content = content;
        this.buttonType = type;
        this.linkedContent = linkedContent;
    }
}
