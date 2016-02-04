package com.nvh.scrabble.view.internaldialpanes;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;

import com.nvh.scrabble.model.Scrabble;
import com.nvh.scrabble.view.MainWindow;

public class ConfirmationPane extends JDialog
{
	private static final long serialVersionUID = 1L;
	private JPanel contentPane;
	JButton btnOk = new JButton("Confirmer");
	JButton btnAnnul = new JButton("Annuler");
	JTextField zoneTexte = new JTextField();
	
	public ConfirmationPane(Scrabble partie)
	{
		setTitle("TIRAGE");
		setModal(true);
		setBounds(600, 460, 469, 193);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		btnOk.setBounds(175, 102, 100, 47);
		contentPane.add(btnOk);
		
		zoneTexte.setSelectionColor(Color.LIGHT_GRAY);
		zoneTexte.setForeground(Color.BLUE);
		zoneTexte.setDisabledTextColor(UIManager.getColor("ToggleButton.light"));
		zoneTexte.setFont(new Font(MainWindow.mainFont, Font.BOLD, 48));
		zoneTexte.setHorizontalAlignment(SwingConstants.CENTER);
		zoneTexte.setText(partie.getHistoTirage().get(partie.getTour()-1));
		zoneTexte.setBounds(10, 11, 443, 80);
		zoneTexte.setEditable(false);
		contentPane.add(zoneTexte);
		
		btnOk.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{// tirage valid�
				dispose();
			}
		});
		
		this.setVisible(true);
	}
	
	
		public ConfirmationPane(Scrabble partie, int j, Scrabble.Solution s) {
			setTitle("Confirmation");
		setModal(true);
		setBounds(600, 460, 469, 193);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		this.setResizable(false);
		this.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);
		
		btnOk.setBounds(322, 102, 100, 47);
		contentPane.add(btnOk);
		zoneTexte.setSelectionColor(Color.LIGHT_GRAY);
		zoneTexte.setForeground(Color.BLUE);
		zoneTexte.setDisabledTextColor(UIManager.getColor("ToggleButton.light"));
		zoneTexte.setFont(new Font(MainWindow.mainFont, Font.PLAIN, 16));
		zoneTexte.setHorizontalAlignment(SwingConstants.CENTER);
		
		if (s.getM().getMot()=="-------") 
			zoneTexte.setText(partie.getJoueur(j).getNom()+" joue un coup invalide");
		else
			
			zoneTexte.setText(partie.getJoueur(j).getNom()+" joue : "+s.getM().getMot()+" en "+s.getM().toCoor());
		
		zoneTexte.setBounds(10, 11, 443, 80);
		zoneTexte.setEditable(false);
		contentPane.add(zoneTexte);
		
		btnAnnul.setBounds(34, 102, 100, 47);
		if (j>0) contentPane.add(btnAnnul);
		btnOk.setVisible(true);
		btnOk.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) 
			{	//pour tout joueur (sauf TOP) on ajoute le coup jou�
				//le coup jou� par TOP est valid� par setSolutions
				if (j>0)
				partie.getJoueur(j).setCoupJoue(s); 
				if (partie.getMainTimer().isKeepCounting()) partie.getMainTimer().arret();
				dispose();
			}
		});

		btnAnnul.addActionListener(new ActionListener() {
			
			public void actionPerformed(ActionEvent e) 
			{// si annul�, pas d'action
				dispose();
			}
		});
		
		this.setVisible(true);
	}
}
