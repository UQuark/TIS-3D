package li.cil.tis3d.client.gui;

import com.mojang.blaze3d.platform.GlStateManager;
import li.cil.tis3d.api.util.RenderUtil;
import li.cil.tis3d.common.Settings;
import li.cil.tis3d.common.module.ModuleTerminal;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.Gui;
import net.minecraft.util.Hand;
import org.lwjgl.glfw.GLFW;
import org.lwjgl.opengl.GL11;

/**
 * Invisible GUI for the terminal module, purely used to grab keyboard input.
 */
@Environment(EnvType.CLIENT)
public final class GuiModuleTerminal extends Gui {
    private final ModuleTerminal module;

    GuiModuleTerminal(final ModuleTerminal module) {
        this.module = module;
    }

    public boolean isFor(final ModuleTerminal that) {
        return that == module;
    }

    @Override
    public void draw(final int mouseX, final int mouseY, final float partialTicks) {
        GlStateManager.disableTexture();

        // To be on the safe side (see manual.Document#render).
        GlStateManager.disableAlphaTest();

        GlStateManager.color4f(1, 1, 1, 1);
        GlStateManager.clear(GL11.GL_DEPTH_BUFFER_BIT, false);
        GlStateManager.enableDepthTest();
        GlStateManager.depthFunc(GL11.GL_LEQUAL);
        GlStateManager.depthMask(true);
        GlStateManager.colorMask(false, false, false, false);
        GlStateManager.pushMatrix();
        GlStateManager.translatef(0, 0, 500);

        RenderUtil.drawUntexturedQuad(8, 8, width - 16, height - 16);

        GlStateManager.popMatrix();
        GlStateManager.depthMask(false);
        GlStateManager.colorMask(true, true, true, true);

        RenderUtil.drawUntexturedQuad(4, 4, width - 8, height - 8);

        GlStateManager.enableTexture();
    }

    @Override
    public boolean keyPressed(int keyCode, int scancode, int mods) {
        if (super.keyPressed(keyCode, scancode, mods)) {
            return true;
        }

        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            return true;
        } else if (keyCode == GLFW.GLFW_KEY_BACKSPACE) {
            return writeToModule('\b');
        } else if (keyCode == GLFW.GLFW_KEY_ENTER || keyCode == GLFW.GLFW_KEY_KP_ENTER) {
            return writeToModule('\n');
        } else if (keyCode == GLFW.GLFW_KEY_TAB) {
            return writeToModule('\t');
        }

        return false;
    }

    @Override
    public boolean charTyped(char chr, int code) {
        if (super.charTyped(chr, code)) {
            return true;
        }

        if (chr != '\0') {
            return writeToModule(chr);
        } else {
            return false;
        }
    }

    private boolean writeToModule(char chr) {
        module.writeToInput(chr);

        if (Settings.animateTypingHand) {
            client.player.swingHand(Hand.MAIN);
        }
        return true;
    }

    @Override
    public void onInitialized() {
        client.keyboard.enableRepeatEvents(true);
    }

    @Override
    public void onClosed() {
        client.keyboard.enableRepeatEvents(false);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}
