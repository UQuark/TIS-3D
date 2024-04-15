package li.cil.tis3d.data.neoforge;

import com.google.common.collect.Sets;
import li.cil.tis3d.api.API;
import li.cil.tis3d.common.block.Blocks;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.*;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public final class ModLootTableProvider extends LootTableProvider {
    public ModLootTableProvider(final PackOutput packOutput) {
        super(packOutput, Collections.emptySet(), Collections.emptyList());
    }

    @Override
    protected void validate(final Map<ResourceLocation, LootTable> map, final ValidationContext validationContext) {
        final Set<ResourceLocation> modLootTableIds =
            BuiltInLootTables
                .all()
                .stream()
                .filter(lootTable -> Objects.equals(lootTable.getNamespace(), API.MOD_ID))
                .collect(Collectors.toSet());

        for (final ResourceLocation id : Sets.difference(modLootTableIds, map.keySet()))
            validationContext.reportProblem("Missing mod loot table: " + id);

        map.forEach((location, table) -> table.validate(validationContext.setParams(table.getParamSet()).enterElement("{" + location + "}", new LootDataId<>(LootDataType.TABLE, location))));
    }

    @Override
    public List<LootTableProvider.SubProviderEntry> getTables() {
        return Collections.singletonList(new SubProviderEntry(ModBlockLootTables::new, LootContextParamSets.BLOCK));
    }

    public static final class ModBlockLootTables extends BlockLootSubProvider {
        public ModBlockLootTables() {
            super(Collections.emptySet(), FeatureFlags.REGISTRY.allFlags());
        }

        @Override
        protected void generate() {
            dropSelf(Blocks.CASING.get());
            dropSelf(Blocks.CONTROLLER.get());
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            return StreamSupport.stream(super.getKnownBlocks().spliterator(), false)
                .filter(block -> {
                    final ResourceLocation blockId = BuiltInRegistries.BLOCK.getKey(block);
                    return Objects.equals(blockId.getNamespace(), API.MOD_ID);
                })
                .collect(Collectors.toSet());
        }
    }
}
