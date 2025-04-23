package review;

/**
 * Review-klassen representerar en recension i webshop-systemet.
 */

public class Review {

    private int reviewId;
    private int customerId;
    private int productId;
    private int rating;
    private String comment;
    private String customerName;
    private String productName;

    // Konstruktor för att skapa ett recensionsobjekt
    public Review(int reviewId, int customerId, int productId, int rating, String comment) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
    }


    public Review(int reviewId, int customerId, int productId, int rating, String comment,
                  String customerName, String productName) {
        this.reviewId = reviewId;
        this.customerId = customerId;
        this.productId = productId;
        this.rating = rating;
        this.comment = comment;
        this.customerName = customerName;
        this.productName = productName;
    }


    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    // Metod för att visa recensionen i form av en sträng
    @Override
    public String toString() {
        String stars = "";
        for (int i = 0; i < rating; i++) {
            stars += "★";
        }
        for (int i = rating; i < 5; i++) {
            stars += "☆";
        }

        return stars + " - " +
                (customerName != null ? customerName : "Anonym") +
                " om " +
                (productName != null ? productName : "Produkt #" + productId) +
                ": " + comment;
    }
}