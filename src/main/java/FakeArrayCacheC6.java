import java.lang.reflect.Array;
import java.util.ArrayDeque;

import com.wynprice.secretroomsmod.optifinehelpers.EOACV;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifine;

import net.minecraft.block.state.IBlockState;

/**
 * Used with Optifine as to help with optifine integration.
 * <br>Only used with Optifine C6
 * @author Wyn Price
 *
 */
@SecretOptifine(version=EOACV.C6)
public class FakeArrayCacheC6 extends ArrayCache
{
	private Class elementClass = null;
    private int maxCacheSize = 0;
    private ArrayDeque cache = new ArrayDeque();

    public FakeArrayCacheC6()
    {
    	super(IBlockState.class, 16);
        this.elementClass = IBlockState.class;
        this.maxCacheSize = 16;
    }
    
    /**
     * Override for this class, returns a new array, of size 0, and class {@link #elementClass}
     */
    public synchronized Object allocate(int p_allocate_1_)
    {
        return Array.newInstance(this.elementClass, 0);
    }
}
