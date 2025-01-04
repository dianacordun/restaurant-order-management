package com.unibuc.java_project.dto;

public class DishTopDTO {
    private Integer rank;
    private Integer orderCount;
    private Long id;
    private String name;
    private Double price;

    // Constructors, getters, and setters
    public DishTopDTO(Integer rank, Integer orderCount, Long id, String name, Double price) {
        this.rank = rank;
        this.orderCount = orderCount;
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getRank() {
        return rank;
    }

    public void setRank(Integer rank) {
        this.rank = rank;
    }

    public Integer getOrderCount() {
        return orderCount;
    }

    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
}