package li.cil.tis3d.client.neoforge;

import li.cil.tis3d.api.API;
import li.cil.tis3d.client.ClientSetup;
import li.cil.tis3d.client.gui.TerminalModuleScreen;
import li.cil.tis3d.client.renderer.block.neoforge.ModuleModelLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.client.event.ModelEvent;
import net.neoforged.neoforge.client.event.RenderGuiOverlayEvent;
import net.neoforged.neoforge.common.NeoForge;

@Mod.EventBusSubscriber(value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD, modid = API.MOD_ID)
public final class ClientSetupNeoForge {
    @SubscribeEvent
    public static void handleClientSetup(final FMLClientSetupEvent ignoredEvent) {
        ClientSetup.run();

        NeoForge.EVENT_BUS.addListener((RenderGuiOverlayEvent.Pre event) -> {
            if (Minecraft.getInstance().screen instanceof TerminalModuleScreen) {
                event.setCanceled(true);
            }
        });
    }

    @SubscribeEvent
    public static void handleModelRegistryEvent(ModelEvent.RegisterGeometryLoaders event) {
        event.register(new ResourceLocation(API.MOD_ID, "module"), new ModuleModelLoader());
    }
}
