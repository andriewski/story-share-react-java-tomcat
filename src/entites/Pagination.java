package entites;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * Created by windmill with love
 * on 17/10/2018.
 */
@Data
@AllArgsConstructor
public class Pagination {
    private long offset;
    private int limit;
}
