package me.thedivazo.libs.dvzconfig.object;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;

@EqualsAndHashCode(callSuper = false)
@Data
@AllArgsConstructor
public class RewardItem extends Reward {
    Map<String, Integer> itemsIdAndCounts;
}
