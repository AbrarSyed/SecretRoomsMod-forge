import java.util.ArrayDeque;

import com.wynprice.secretroomsmod.optifinehelpers.EOACV;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifine;
import com.wynprice.secretroomsmod.optifinehelpers.SecretOptifineHelper;

import net.minecraft.block.state.IBlockState;

@SecretOptifine(version=EOACV.C7)
public class FakeArrayCacheC7 extends ArrayCache
{
	private Class elementClass = null;
    private int maxCacheSize = 0;
    private ArrayDeque cache = new ArrayDeque();

    public FakeArrayCacheC7()
    {
    	super(IBlockState.class, 16);
        this.elementClass = IBlockState.class;
        this.maxCacheSize = 16;
    }

    public synchronized Object allocate(int p_allocate_1_)
    {
        IBlockState[] list = (IBlockState[]) super.allocate(p_allocate_1_);
        SecretOptifineHelper.CURRENT_C7_LIST.add(list);
        return list;
    }
    
    public synchronized void free(Object p_free_1_) 
    {
    	super.free(p_free_1_);
    	SecretOptifineHelper.CURRENT_C7_LIST.remove(p_free_1_);
    }
}
