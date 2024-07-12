package net.danygames2014.gambac.mixin;

import net.danygames2014.gambac.BrnoMinecraft;
import net.minecraft.client.Minecraft;
import net.minecraft.client.MinecraftApplet;
import net.minecraft.client.util.Session;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import javax.swing.*;
import java.applet.Applet;
import java.awt.*;

@SuppressWarnings("removal")
@Mixin(MinecraftApplet.class)
public class MinecraftAppletMixin extends Applet {

    @Shadow private Minecraft field_2832;

    /**
     * @author Proudly overwritten by DanyGames2014
     * @reason because i don't give a shit
     */
    @Overwrite(remap = false)
    public void init(){
        boolean var1 = false;
        if (this.getParameter("fullscreen") != null) {
            var1 = this.getParameter("fullscreen").equalsIgnoreCase("true");
        }

        this.field_2832 = new BrnoMinecraft(this.getWidth(), this.getHeight(), var1);
        this.field_2832.field_2810 = this.getDocumentBase().getHost();
        if (this.getDocumentBase().getPort() > 0) {
            StringBuilder var10000 = new StringBuilder();
            Minecraft var10002 = this.field_2832;
            var10002.field_2810 = var10000.append(var10002.field_2810).append(":").append(this.getDocumentBase().getPort()).toString();
        }

        if (this.getParameter("username") != null && this.getParameter("sessionid") != null) {
            this.field_2832.session = new Session(this.getParameter("username"), this.getParameter("sessionid"));
            System.out.println("Setting user: " + this.field_2832.session.username);
            if (this.getParameter("mppass") != null) {
                this.field_2832.session.mpPass = this.getParameter("mppass");
            }
        } else {
            this.field_2832.session = new Session("Payer" + System.currentTimeMillis() % 10000, "");
        }

        if (this.getParameter("server") != null && this.getParameter("port") != null) {
            this.field_2832.method_2117(this.getParameter("server"), Integer.parseInt(this.getParameter("port")));
        }

        SwingUtilities.invokeLater(() -> {
            hideThemAll(this.getParent().getParent().getParent());
            hideThemAll(this.getParent().getParent());
            hideThemAll(this.getParent());
            hideThemAll(this);
            this.method_2153();
        });
    }

    @Unique
    private void hideThemAll(Container container) {
        try {
            if (container instanceof Frame) {
                ((Frame) container).dispose();
            }
            for (Component component : container.getComponents()) {
                component.setVisible(false);
            }
        } catch (NullPointerException ignore) {
        }
    }

    /**
     * @author DanyGames2014
     * @reason because i don't give a shit
     */
    @Overwrite
    public void method_2153() { // startMainThread
        this.field_2832.run();
    }

    @Inject(method = "destroy", at = @At(value = "HEAD"), remap = false, cancellable = true)
    public void destroy(CallbackInfo ci) {
        ci.cancel();
    }

    @Inject(method = "method_2154", at= @At(value = "HEAD"), cancellable = true)
    public void method_2154(CallbackInfo ci) { // onRemoveNotify / shutdown
        ci.cancel();
    }

    @Inject(method = "method_2155", at = @At(value = "HEAD"), cancellable = true)
    public void method_2155(CallbackInfo ci) { // clearApplet
        ci.cancel();
    }
}
