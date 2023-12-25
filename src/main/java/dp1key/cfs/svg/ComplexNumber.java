package dp1key.cfs.svg;

@SuppressWarnings({"unused"})
public class ComplexNumber {

    public static final int XY = 0;
    public static final int RCIS = 1;


    private double real;
    private double imaginary;

    public ComplexNumber() {
        this.real = 0.0;
        this.imaginary = 0.0;
    }

    public ComplexNumber(double real, double imaginary) {
        this.real = real;
        this.imaginary = imaginary;
    }

    public void add(ComplexNumber z) {
        this.set(ComplexNumber.add(this, z));
    }

    public void subtract(ComplexNumber z) {
        this.set(ComplexNumber.subtract(this, z));
    }

    public void multiply(ComplexNumber z) {
        this.set(ComplexNumber.multiply(this, z));
    }

    public void divide(ComplexNumber z) {
        this.set(ComplexNumber.divide(this, z));
    }

    public void add(double n) {
        this.set(ComplexNumber.add(this, n));
    }

    public void multiply(double n) {
        this.set(ComplexNumber.multiply(this, n));
    }

    public void divide(double n) {
        this.set(ComplexNumber.divide(this, n));
    }

    public ComplexNumber conjugate() {
        return new ComplexNumber(this.real, -this.imaginary);
    }

    public double mod() {
        return Math.sqrt(Math.pow(this.real, 2) + Math.pow(this.imaginary, 2));
    }

    public ComplexNumber square() {
        double _real = this.real * this.real - this.imaginary * this.imaginary;
        double _imaginary = 2 * this.real * this.imaginary;
        return new ComplexNumber(_real, _imaginary);
    }

    public static ComplexNumber exp(ComplexNumber z) {
        double r = Math.exp(z.real);
        double _real = r * Math.cos(z.imaginary);
        double _imaginary = r * Math.sin(z.imaginary);
        return new ComplexNumber(_real, _imaginary);
    }

    public static ComplexNumber pow(ComplexNumber z, int power) {
        ComplexNumber output = new ComplexNumber(z.getReal(), z.getImaginary());
        for (int i = 1; i < power; i++) {
            double _real = output.real * z.real - output.imaginary * z.imaginary;
            double _imaginary = output.real * z.imaginary + output.imaginary * z.real;
            output = new ComplexNumber(_real, _imaginary);
        }
        return output;
    }

    public static ComplexNumber sin(ComplexNumber z) {
        double x = Math.exp(z.imaginary);
        double x_inv = 1 / x;
        double r = Math.sin(z.real) * (x + x_inv) / 2;
        double i = Math.cos(z.real) * (x - x_inv) / 2;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber cos(ComplexNumber z) {
        double x = Math.exp(z.imaginary);
        double x_inv = 1 / x;
        double r = Math.cos(z.real) * (x + x_inv) / 2;
        double i = -Math.sin(z.real) * (x - x_inv) / 2;
        return new ComplexNumber(r, i);
    }

    public static ComplexNumber tan(ComplexNumber z) {
        return ComplexNumber.divide(ComplexNumber.sin(z), ComplexNumber.cos(z));
    }

    public static ComplexNumber cot(ComplexNumber z) {
        return ComplexNumber.divide(new ComplexNumber(1, 0), ComplexNumber.tan(z));
    }

    public static ComplexNumber sec(ComplexNumber z) {
        return ComplexNumber.divide(new ComplexNumber(1, 0), ComplexNumber.cos(z));
    }

    public static ComplexNumber cosec(ComplexNumber z) {
        return ComplexNumber.divide(new ComplexNumber(1, 0), ComplexNumber.sin(z));
    }

    /**
     * The argument/phase of the current complex number.
     * @return arg(z) - the argument of current complex number
     */
    public double getArg() {
        return Math.atan2(imaginary, real);
    }

    public ComplexNumber inverse() {
        return ComplexNumber.divide(new ComplexNumber(1,0), this);
    }

    public static ComplexNumber add(ComplexNumber z1, ComplexNumber z2) {
        return new ComplexNumber(z1.real + z2.real, z1.imaginary + z2.imaginary);
    }

    public static ComplexNumber subtract(ComplexNumber z1, ComplexNumber z2) {
        return new ComplexNumber(z1.real - z2.real, z1.imaginary - z2.imaginary);
    }

    public static ComplexNumber multiply(ComplexNumber z1, ComplexNumber z2) {
        double _real = z1.real * z2.real - z1.imaginary * z2.imaginary;
        double _imaginary = z1.real * z2.imaginary + z1.imaginary * z2.real;
        return new ComplexNumber(_real, _imaginary);
    }

    public static ComplexNumber divide(ComplexNumber z1, ComplexNumber z2) {
        ComplexNumber output = multiply(z1, z2.conjugate());
        double div = Math.pow(z2.mod(), 2);
        return new ComplexNumber(output.real / div, output.imaginary / div);
    }

    public static ComplexNumber add(ComplexNumber z, double n) {
        return new ComplexNumber(z.getReal() + n, z.getImaginary());
    }

    public static ComplexNumber multiply(ComplexNumber z, double n) {
        return new ComplexNumber(z.getReal() * n, z.getImaginary() * n);
    }

    public static ComplexNumber divide(ComplexNumber z, double n) {
        return new ComplexNumber(z.getReal() / n, z.getImaginary() / n);
    }

    public static ComplexNumber parseComplex(String str) {
        str = str.replaceAll(" ","");
        if(str.contains("+") || (str.contains("-") && str.lastIndexOf('-') > 0)) {

            str = str.replaceAll("i","").replaceAll("I", "");

            if (str.indexOf('+') > 0) {
                String re = str.substring(0, str.indexOf('+'));
                String im = str.substring(str.indexOf('+') + 1);
                return new ComplexNumber(Double.parseDouble(re), Double.parseDouble(im));
            } else if (str.lastIndexOf('-') > 0) {
                String re = str.substring(0, str.lastIndexOf('-'));
                String im = str.substring(str.lastIndexOf('-') + 1);
                return new ComplexNumber(Double.parseDouble(re), -Double.parseDouble(im));
            }
        }

        // Pure imaginary number
        if(str.endsWith("i") || str.endsWith("I")) {
            str = str.replaceAll("i","").replaceAll("I","");
            return new ComplexNumber(0, Double.parseDouble(str));
        }

        // Pure real number
        return new ComplexNumber(Double.parseDouble(str),0);

    }

    @Override
    public String toString() {
        String realPart = this.real + "";
        String imaginaryPart = this.imaginary + "i";
        if (this.imaginary >= 0) imaginaryPart = "+" + imaginaryPart;
        return realPart + imaginaryPart;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ComplexNumber that = (ComplexNumber) o;
        return Double.compare(getReal(), that.getReal()) == 0 && Double.compare(getImaginary(), that.getImaginary()) == 0;
    }

    public String format(int formatId) throws IllegalArgumentException {
        if(formatId == XY) return this.toString();
        if(formatId == RCIS) return this.mod() + " cis(" + this.getArg() + ")";
        throw new IllegalArgumentException("Unknown Complex Number format.");
    }

    public void set(ComplexNumber z) {
        this.real = z.real;
        this.imaginary = z.imaginary;
    }

    public double getReal() {
        return real;
    }

    public double getImaginary() {
        return imaginary;
    }
}
