package com.example;

import java.awt.BorderLayout;
import java.io.File;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileNameExtensionFilter;
import net.runelite.client.config.ConfigManager;
import net.runelite.client.ui.PluginPanel;

public class MmoHudPanel extends PluginPanel
{
    private final ConfigManager configManager;
    private final PortraitProvider portraitProvider;
    private final JLabel selectedFileLabel = new JLabel();

    public MmoHudPanel(ConfigManager configManager, PortraitProvider portraitProvider)
    {
        this.configManager = configManager;
        this.portraitProvider = portraitProvider;

        setLayout(new BorderLayout(0, 8));

        JButton chooseImageButton = new JButton("Choose Portrait Image");
        chooseImageButton.addActionListener(e -> chooseImage());

        selectedFileLabel.setText(getCurrentPathText());

        JPanel top = new JPanel(new BorderLayout(0, 8));
        top.add(chooseImageButton, BorderLayout.NORTH);
        top.add(selectedFileLabel, BorderLayout.CENTER);

        add(top, BorderLayout.NORTH);
    }

    private void chooseImage()
    {
        JFileChooser chooser = new JFileChooser();
        chooser.setDialogTitle("Choose portrait image");
        chooser.setFileFilter(new FileNameExtensionFilter("Image files", "png", "jpg", "jpeg", "gif"));

        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION)
        {
            return;
        }

        File file = chooser.getSelectedFile();

        if (file == null)
        {
            return;
        }

        configManager.setConfiguration(ExampleConfig.GROUP, "portraitImagePath", file.getAbsolutePath());
        selectedFileLabel.setText(file.getName());
        portraitProvider.reload();
    }

    private String getCurrentPathText()
    {
        String path = configManager.getConfiguration(ExampleConfig.GROUP, "portraitImagePath");

        if (path == null || path.trim().isEmpty())
        {
            return "No image selected";
        }

        return new File(path).getName();
    }
}