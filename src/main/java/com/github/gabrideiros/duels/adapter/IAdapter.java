package com.github.gabrideiros.duels.adapter;

import org.bukkit.configuration.ConfigurationSection;

import java.util.List;
import java.util.Map;

public interface IAdapter<T> {

    T parse(ConfigurationSection section);

    List<T> parseList(ConfigurationSection section);
}
