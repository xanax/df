package uk.co.gosseyn.xanax.view.web;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@FieldDefaults(level= AccessLevel.PRIVATE)
public class FrameData {
    int tiles[];
    int heights[];
    final Map<String, BlockData> blockData = new HashMap<>();
    BigInteger frame = BigInteger.ZERO;
}
