package me.thedivazo.libs.dvzconfig.object;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.spongepowered.configurate.objectmapping.ConfigSerializable;

import java.util.List;
import java.util.Map;

@ConfigSerializable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class TestConfigThree {
    String title = "Give Reward";

    List<Reward> rewards = List.of(
            new RewardItem(Map.of("sword", 45, "fuel", 2)),
            new RewardEffect(List.of("SPEED", "SLOW"), 300d),
            new RewardItem(Map.of("axe", 415, "turret", 2))
    );


}
