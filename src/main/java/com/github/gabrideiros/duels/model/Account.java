package com.github.gabrideiros.duels.model;

import lombok.Builder;
import lombok.Data;

import java.util.UUID;

@Data
@Builder
public class Account {

    private UUID uuid;
    private int wins, loss, kills, deaths;
}
