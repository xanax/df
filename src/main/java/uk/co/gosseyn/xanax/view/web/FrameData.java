package uk.co.gosseyn.xanax.view.web;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
@Setter
public class FrameData {
    private int tiles[];
    private int heights[];
    private Map<String, BlockData> blockData = new HashMap<>();
}
