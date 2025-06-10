package me.thedivazo.libs.dvzconfig.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class RewardEffect extends Reward {
    List<String> effects;
    Double durable;
}
