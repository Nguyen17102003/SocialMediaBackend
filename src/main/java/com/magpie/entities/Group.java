package com.magpie.entities;

import jakarta.persistence.*;
import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "groups")
public class Group {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(unique = true, nullable = false, updatable = false)
    private UUID publicId = UUID.randomUUID();

    @Column(nullable = false, updatable = false)
    private UUID founderId;

    @ManyToMany(mappedBy = "groups", fetch = FetchType.LAZY)
    private List<User> members;

    @Column(unique = true)
    private String groupName;
    private String groupDescription;
    private String groupImage;

    @Column(nullable = false)
    private Boolean isPublic;

    @Column(nullable = false, updatable = false)
    private Timestamp groupCreated = new Timestamp(System.currentTimeMillis());;
}
