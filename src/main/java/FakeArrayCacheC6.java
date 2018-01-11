import java.lang.reflect.Array;
import java.util.ArrayDeque;

import com.wynprice.secretroomsmod.optifinehelpers.EOACV;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifine;

import net.minecraft.block.state.IBlockState;

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

    public synchronized Object allocate(int p_allocate_1_)
    {
        return Array.newInstance(this.elementClass, 0);
    }

    public synchronized void free(Object p_free_1_)
    {
        if (p_free_1_ != null)
        {
            Class oclass = p_free_1_.getClass();

            if (oclass.getComponentType() != this.elementClass)
            {
                throw new IllegalArgumentException("Wrong component type");
            }
            else if (this.cache.size() < this.maxCacheSize)
            {
                this.cache.add(p_free_1_);
            }
        }
    }
}
