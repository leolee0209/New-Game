package Main;

public class Vector {

    public static final Vector one = new Vector(1, 1);
    public static final Vector zero = new Vector(0, 0);
    public static final Vector negone = new Vector(-1, -1);

    public int x, y;

    public Vector() {
    }

    public Vector(int x, int y) {
        SetVector(x, y);
    }
    public Vector(Vector vector) {
        SetVector(vector);
    }

    public void SetVector(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void SetVector(Vector pos) {
        this.x = pos.x;
        this.y = pos.y;
    }

    public boolean equals(Vector vector2) {
        if (this.x == vector2.x && this.y == vector2.y) {
            return true;
        }
        else {
            return false;
        }
    }

    public Vector Add(Vector vector2) {
        return new Vector(this.x + vector2.x, this.y + vector2.y);
    }

    public Vector Minus(Vector vector2) {
        return new Vector(this.x - vector2.x, this.y - vector2.y);
    }

    public Vector Multiply(int mul) {
        return new Vector(this.x * mul, this.y * mul);
    }

    public Vector Divide(int div) {
        if (div != 0) {
            return new Vector(this.x / div, this.y / div);
        }
        return null;
    }

    public void AddEqual(Vector vector2) {
        this.x += vector2.x;
        this.y += vector2.y;
    }

    public static Vector Add(Vector vector1, Vector vector2) {
        return new Vector(vector1.x + vector2.x, vector1.y + vector2.y);
    }
    
    public static Vector Minus(Vector vector1, Vector vector2) {
        return new Vector(vector1.x - vector2.x, vector1.y - vector2.y);
    }
    
    public static Vector Multiply(Vector vector1, int mul) {
        return new Vector(vector1.x * mul, vector1.y * mul);
    }
    
    public void Print() {
        System.out.print(this.FormatToBrackets());
    }

    public String FormatToBrackets() {
        return "(" + this.x + "," + this.y + ")";
    }

    public boolean isInsideSquare(Vector beginPoint, Vector size) {
        if (this.x >= beginPoint.x && this.y >= beginPoint.y && this.x <= beginPoint.x + size.x
                && this.y <= beginPoint.y + size.y) {
            return true;
        }
        return false;
    }
}
