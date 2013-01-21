; Script generated by the HM NIS Edit Script Wizard.

; HM NIS Edit Wizard helper defines
!define PRODUCT_NAME "Battle Hub"
!define PRODUCT_VERSION "tba"
!define PRODUCT_PUBLISHER "Tom J Nowell AF Darkstars"
!define PRODUCT_WEB_SITE "http://www.darkstars.co.uk"
!define PRODUCT_DIR_REGKEY "Software\Microsoft\Windows\CurrentVersion\App Paths\${PRODUCT_NAME}"
!define PRODUCT_UNINST_KEY "Software\Microsoft\Windows\CurrentVersion\Uninstall\${PRODUCT_NAME}"
!define PRODUCT_UNINST_ROOT_KEY "HKLM"
!define PRODUCT_STARTMENU_REGVAL "NSIS:StartMenuDir"

; MUI 1.67 compatible ------
!include "MUI.nsh"

; MUI Settings
!define MUI_ABORTWARNING
!define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\modern-install.ico"
!define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\modern-uninstall.ico"
!define MUI_WELCOMEFINISHPAGE_BITMAP "NSIS_sidebanner.bmp"
!define MUI_HEADERIMAGE
!define MUI_HEADERIMAGE_BITMAP "NSIS_toplogo.bmp"

; Welcome page
!insertmacro MUI_PAGE_WELCOME
; License page
!insertmacro MUI_PAGE_LICENSE "gpl.txt"
; Directory page
!insertmacro MUI_PAGE_DIRECTORY
; Start menu page
var ICONS_GROUP
!define MUI_STARTMENUPAGE_NODISABLE
!define MUI_STARTMENUPAGE_DEFAULTFOLDER "Spring"
!define MUI_STARTMENUPAGE_REGISTRY_ROOT "${PRODUCT_UNINST_ROOT_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_KEY "${PRODUCT_UNINST_KEY}"
!define MUI_STARTMENUPAGE_REGISTRY_VALUENAME "${PRODUCT_STARTMENU_REGVAL}"
!insertmacro MUI_PAGE_STARTMENU Application $ICONS_GROUP
; Instfiles page
!insertmacro MUI_PAGE_INSTFILES
; Finish page
!define MUI_FINISHPAGE_RUN "javaw.exe -jar $INSTDIR\BattleHub.jar"
!insertmacro MUI_PAGE_FINISH

; Uninstaller pages
!insertmacro MUI_UNPAGE_INSTFILES

; Language files
!insertmacro MUI_LANGUAGE "English"

; MUI end ------

Name "${PRODUCT_NAME} ${PRODUCT_VERSION}"
OutFile "Setup.exe"
InstallDir "$PROGRAMFILES\Spring"
InstallDirRegKey HKLM "${PRODUCT_DIR_REGKEY}" ""
ShowInstDetails show
ShowUnInstDetails show

Section "MainSection" SEC01
  SetOutPath "$INSTDIR"
  SetOverwrite try
  File "..\dist\BattleHub.jar"
  File "AFL.ico"
  SetOutPath "$INSTDIR\lib"
  File "..\dist\lib\substance.jar"
  File "..\dist\lib\flamingo.jar"
  SetOutPath "$INSTDIR\lobby\BattleHub\images\flags"
  File "..\package1\images\flags\AD.png"
  File "..\package1\images\flags\AE.png"
  File "..\package1\images\flags\AF.png"
  File "..\package1\images\flags\AG.png"
  File "..\package1\images\flags\AI.png"
  File "..\package1\images\flags\AL.png"
  File "..\package1\images\flags\AM.png"
  File "..\package1\images\flags\AN.png"
  File "..\package1\images\flags\AO.png"
  File "..\package1\images\flags\AP.png"
  File "..\package1\images\flags\AQ.png"
  File "..\package1\images\flags\AR.png"
  File "..\package1\images\flags\AS.png"
  File "..\package1\images\flags\AT.png"
  File "..\package1\images\flags\AU.png"
  File "..\package1\images\flags\AW.png"
  File "..\package1\images\flags\AZ.png"
  File "..\package1\images\flags\BA.png"
  File "..\package1\images\flags\BB.png"
  File "..\package1\images\flags\BD.png"
  File "..\package1\images\flags\BE.png"
  File "..\package1\images\flags\BF.png"
  File "..\package1\images\flags\BG.png"
  File "..\package1\images\flags\BH.png"
  File "..\package1\images\flags\BI.png"
  File "..\package1\images\flags\BJ.png"
  File "..\package1\images\flags\BM.png"
  File "..\package1\images\flags\BN.png"
  File "..\package1\images\flags\BO.png"
  File "..\package1\images\flags\BR.png"
  File "..\package1\images\flags\BS.png"
  File "..\package1\images\flags\BT.png"
  File "..\package1\images\flags\BV.png"
  File "..\package1\images\flags\BW.png"
  File "..\package1\images\flags\BY.png"
  File "..\package1\images\flags\BZ.png"
  File "..\package1\images\flags\CA.png"
  File "..\package1\images\flags\CC.png"
  File "..\package1\images\flags\CD.png"
  File "..\package1\images\flags\CF.png"
  File "..\package1\images\flags\CG.png"
  File "..\package1\images\flags\CH.png"
  File "..\package1\images\flags\CI.png"
  File "..\package1\images\flags\CK.png"
  File "..\package1\images\flags\CL.png"
  File "..\package1\images\flags\CM.png"
  File "..\package1\images\flags\CN.png"
  File "..\package1\images\flags\CO.png"
  File "..\package1\images\flags\CR.png"
  File "..\package1\images\flags\CS.png"
  File "..\package1\images\flags\CU.png"
  File "..\package1\images\flags\CV.png"
  File "..\package1\images\flags\CX.png"
  File "..\package1\images\flags\CY.png"
  File "..\package1\images\flags\CZ.png"
  File "..\package1\images\flags\DE.png"
  File "..\package1\images\flags\DJ.png"
  File "..\package1\images\flags\DK.png"
  File "..\package1\images\flags\DM.png"
  File "..\package1\images\flags\DO.png"
  File "..\package1\images\flags\DZ.png"
  File "..\package1\images\flags\EC.png"
  File "..\package1\images\flags\EE.png"
  File "..\package1\images\flags\EG.png"
  File "..\package1\images\flags\EH.png"
  File "..\package1\images\flags\ER.png"
  File "..\package1\images\flags\ES.png"
  File "..\package1\images\flags\ET.png"
  File "..\package1\images\flags\FI.png"
  File "..\package1\images\flags\FJ.png"
  File "..\package1\images\flags\FK.png"
  File "..\package1\images\flags\FM.png"
  File "..\package1\images\flags\FO.png"
  File "..\package1\images\flags\FR.png"
  File "..\package1\images\flags\GA.png"
  File "..\package1\images\flags\GB.png"
  File "..\package1\images\flags\GD.png"
  File "..\package1\images\flags\GE.png"
  File "..\package1\images\flags\GF.png"
  File "..\package1\images\flags\GH.png"
  File "..\package1\images\flags\GI.png"
  File "..\package1\images\flags\GL.png"
  File "..\package1\images\flags\GM.png"
  File "..\package1\images\flags\GN.png"
  File "..\package1\images\flags\GP.png"
  File "..\package1\images\flags\GQ.png"
  File "..\package1\images\flags\GR.png"
  File "..\package1\images\flags\GS.png"
  File "..\package1\images\flags\GT.png"
  File "..\package1\images\flags\GU.png"
  File "..\package1\images\flags\GW.png"
  File "..\package1\images\flags\GY.png"
  File "..\package1\images\flags\HK.png"
  File "..\package1\images\flags\HM.png"
  File "..\package1\images\flags\HN.png"
  File "..\package1\images\flags\HR.png"
  File "..\package1\images\flags\HT.png"
  File "..\package1\images\flags\HU.png"
  File "..\package1\images\flags\ID.png"
  File "..\package1\images\flags\IE.png"
  File "..\package1\images\flags\IL.png"
  File "..\package1\images\flags\IN.png"
  File "..\package1\images\flags\IO.png"
  File "..\package1\images\flags\IQ.png"
  File "..\package1\images\flags\IR.png"
  File "..\package1\images\flags\IS.png"
  File "..\package1\images\flags\IT.png"
  File "..\package1\images\flags\JM.png"
  File "..\package1\images\flags\JO.png"
  File "..\package1\images\flags\JP.png"
  File "..\package1\images\flags\KE.png"
  File "..\package1\images\flags\KG.png"
  File "..\package1\images\flags\KH.png"
  File "..\package1\images\flags\KI.png"
  File "..\package1\images\flags\KM.png"
  File "..\package1\images\flags\KN.png"
  File "..\package1\images\flags\KP.png"
  File "..\package1\images\flags\KR.png"
  File "..\package1\images\flags\KW.png"
  File "..\package1\images\flags\KY.png"
  File "..\package1\images\flags\KZ.png"
  File "..\package1\images\flags\LA.png"
  File "..\package1\images\flags\LB.png"
  File "..\package1\images\flags\LC.png"
  File "..\package1\images\flags\LI.png"
  File "..\package1\images\flags\LK.png"
  File "..\package1\images\flags\LR.png"
  File "..\package1\images\flags\LS.png"
  File "..\package1\images\flags\LT.png"
  File "..\package1\images\flags\LU.png"
  File "..\package1\images\flags\LV.png"
  File "..\package1\images\flags\LY.png"
  File "..\package1\images\flags\MA.png"
  File "..\package1\images\flags\MC.png"
  File "..\package1\images\flags\MD.png"
  File "..\package1\images\flags\MG.png"
  File "..\package1\images\flags\MH.png"
  File "..\package1\images\flags\MK.png"
  File "..\package1\images\flags\ML.png"
  File "..\package1\images\flags\MM.png"
  File "..\package1\images\flags\MN.png"
  File "..\package1\images\flags\MO.png"
  File "..\package1\images\flags\MP.png"
  File "..\package1\images\flags\MQ.png"
  File "..\package1\images\flags\MR.png"
  File "..\package1\images\flags\MS.png"
  File "..\package1\images\flags\MT.png"
  File "..\package1\images\flags\MU.png"
  File "..\package1\images\flags\MV.png"
  File "..\package1\images\flags\MW.png"
  File "..\package1\images\flags\MX.png"
  File "..\package1\images\flags\MY.png"
  File "..\package1\images\flags\MZ.png"
  File "..\package1\images\flags\NA.png"
  File "..\package1\images\flags\NC.png"
  File "..\package1\images\flags\NE.png"
  File "..\package1\images\flags\NF.png"
  File "..\package1\images\flags\NG.png"
  File "..\package1\images\flags\NI.png"
  File "..\package1\images\flags\NL.png"
  File "..\package1\images\flags\NO.png"
  File "..\package1\images\flags\NP.png"
  File "..\package1\images\flags\NR.png"
  File "..\package1\images\flags\NU.png"
  File "..\package1\images\flags\NZ.png"
  File "..\package1\images\flags\OM.png"
  File "..\package1\images\flags\PA.png"
  File "..\package1\images\flags\PE.png"
  File "..\package1\images\flags\PF.png"
  File "..\package1\images\flags\PG.png"
  File "..\package1\images\flags\PH.png"
  File "..\package1\images\flags\PK.png"
  File "..\package1\images\flags\PL.png"
  File "..\package1\images\flags\PM.png"
  File "..\package1\images\flags\PN.png"
  File "..\package1\images\flags\PR.png"
  File "..\package1\images\flags\PS.png"
  File "..\package1\images\flags\PT.png"
  File "..\package1\images\flags\PW.png"
  File "..\package1\images\flags\PY.png"
  File "..\package1\images\flags\QA.png"
  File "..\package1\images\flags\RE.png"
  File "..\package1\images\flags\RO.png"
  File "..\package1\images\flags\RU.png"
  File "..\package1\images\flags\RW.png"
  File "..\package1\images\flags\SA.png"
  File "..\package1\images\flags\SB.png"
  File "..\package1\images\flags\SC.png"
  File "..\package1\images\flags\SD.png"
  File "..\package1\images\flags\SE.png"
  File "..\package1\images\flags\SG.png"
  File "..\package1\images\flags\SH.png"
  File "..\package1\images\flags\SI.png"
  File "..\package1\images\flags\SJ.png"
  File "..\package1\images\flags\SK.png"
  File "..\package1\images\flags\SL.png"
  File "..\package1\images\flags\SM.png"
  File "..\package1\images\flags\SN.png"
  File "..\package1\images\flags\SO.png"
  File "..\package1\images\flags\SR.png"
  File "..\package1\images\flags\ST.png"
  File "..\package1\images\flags\SV.png"
  File "..\package1\images\flags\SY.png"
  File "..\package1\images\flags\SZ.png"
  File "..\package1\images\flags\TC.png"
  File "..\package1\images\flags\TD.png"
  File "..\package1\images\flags\TF.png"
  File "..\package1\images\flags\TG.png"
  File "..\package1\images\flags\TH.png"
  File "..\package1\images\flags\TJ.png"
  File "..\package1\images\flags\TK.png"
  File "..\package1\images\flags\TL.png"
  File "..\package1\images\flags\TM.png"
  File "..\package1\images\flags\TN.png"
  File "..\package1\images\flags\TO.png"
  File "..\package1\images\flags\TR.png"
  File "..\package1\images\flags\TT.png"
  File "..\package1\images\flags\TV.png"
  File "..\package1\images\flags\TW.png"
  File "..\package1\images\flags\TZ.png"
  File "..\package1\images\flags\UA.png"
  File "..\package1\images\flags\UG.png"
  File "..\package1\images\flags\UM.png"
  File "..\package1\images\flags\US.png"
  File "..\package1\images\flags\UY.png"
  File "..\package1\images\flags\UZ.png"
  File "..\package1\images\flags\VA.png"
  File "..\package1\images\flags\VC.png"
  File "..\package1\images\flags\VE.png"
  File "..\package1\images\flags\VG.png"
  File "..\package1\images\flags\VI.png"
  File "..\package1\images\flags\VN.png"
  File "..\package1\images\flags\VU.png"
  File "..\package1\images\flags\WF.png"
  File "..\package1\images\flags\WS.png"
  File "..\package1\images\flags\YE.png"
  File "..\package1\images\flags\YT.png"
  File "..\package1\images\flags\YU.png"
  File "..\package1\images\flags\ZA.png"
  File "..\package1\images\flags\ZM.png"
  File "..\package1\images\flags\ZW.png"
  SetOutPath "$INSTDIR\lobby\BattleHub\images"
  File "..\package1\images\icon.png"
  SetOutPath "$INSTDIR\lobby\BattleHub\images\ranks"
  File "..\package1\images\ranks\0.png"
  File "..\package1\images\ranks\1.png"
  File "..\package1\images\ranks\2.png"
  File "..\package1\images\ranks\3.png"
  File "..\package1\images\ranks\4.png"
  SetOutPath "$INSTDIR\lobby\BattleHub\images\ranks_small"
  File "..\package1\images\ranks_small\0.png"
  File "..\package1\images\ranks_small\1.png"
  File "..\package1\images\ranks_small\2.png"
  File "..\package1\images\ranks_small\3.png"
  File "..\package1\images\ranks_small\4.png"
  SetOutPath "$INSTDIR\lobby\BattleHub\images\smileys"
  File "..\package1\images\smileys\afro.gif"
  File "..\package1\images\smileys\asdf.gif"
  File "..\package1\images\smileys\badger.gif"
  File "..\package1\images\smileys\beta.gif"
  File "..\package1\images\smileys\blies.gif"
  File "..\package1\images\smileys\bowl.gif"
  File "..\package1\images\smileys\brtfm.gif"
  File "..\package1\images\smileys\bty.gif"
  File "..\package1\images\smileys\cesure.gif"
  File "..\package1\images\smileys\character0100.gif"
  File "..\package1\images\smileys\character0109.gif"
  File "..\package1\images\smileys\character0160.gif"
  File "..\package1\images\smileys\coffee.gif"
  File "..\package1\images\smileys\cool.gif"
  File "..\package1\images\smileys\crazy.gif"
  File "..\package1\images\smileys\cry.gif"
  File "..\package1\images\smileys\evil.gif"
  File "..\package1\images\smileys\evilimu.gif"
  File "..\package1\images\smileys\fireworks.gif"
  File "..\package1\images\smileys\flour.gif"
  File "..\package1\images\smileys\freak.gif"
  File "..\package1\images\smileys\gundam.gif"
  File "..\package1\images\smileys\hamtaro.gif"
  File "..\package1\images\smileys\happy.gif"
  File "..\package1\images\smileys\headbash.gif"
  File "..\package1\images\smileys\hide.gif"
  File "..\package1\images\smileys\imslow.gif"
  File "..\package1\images\smileys\Kirby.gif"
  File "..\package1\images\smileys\lurk2.gif"
  File "..\package1\images\smileys\nixpenguin.gif"
  File "..\package1\images\smileys\notfuneh.gif"
  File "..\package1\images\smileys\offtopic.gif"
  File "..\package1\images\smileys\pikachu-head.gif"
  File "..\package1\images\smileys\pikachu.gif"
  File "..\package1\images\smileys\puke.gif"
  File "..\package1\images\smileys\redface.gif"
  File "..\package1\images\smileys\roillingcat.gif"
  File "..\package1\images\smileys\rubberducky.gif"
  File "..\package1\images\smileys\sadmonk.gif"
  File "..\package1\images\smileys\sign0030.gif"
  File "..\package1\images\smileys\sign0033.gif"
  File "..\package1\images\smileys\sign0036.gif"
  File "..\package1\images\smileys\sign0049.gif"
  File "..\package1\images\smileys\sign0073.gif"
  File "..\package1\images\smileys\sign0082.gif"
  File "..\package1\images\smileys\sign0085.gif"
  File "..\package1\images\smileys\sign0094.gif"
  File "..\package1\images\smileys\soldier.gif"
  File "..\package1\images\smileys\spammer.gif"
  File "..\package1\images\smileys\stunned.gif"
  File "..\package1\images\smileys\stupid.gif"
  File "..\package1\images\smileys\thumbup.gif"
  File "..\package1\images\smileys\toiletclaw.gif"
  File "..\package1\images\smileys\toothy4.gif"
  File "..\package1\images\smileys\travolta.gif"
  File "..\package1\images\smileys\twisted.gif"
  File "..\package1\images\smileys\worship2.gif"
  File "..\package1\images\smileys\yay.gif"
  File "..\package1\images\smileys\yourock.gif"
  File "..\package1\images\smileys\twosheep.gif"
  File "..\package1\images\smileys\_bbq.gif"
  SetOutPath "$INSTDIR\lobby\battlehub\images\UI"
  File "..\package1\images\UI\alarm.gif"
  File "..\package1\images\UI\icon.png"
  SetOutPath "$INSTDIR\lobby\battlehub\sounds"
  File "..\package1\sounds\question.wav"
  SetOutPath "$INSTDIR\lobby\battlehub\minimaps"
  File "..\package1\minimaps\tangerine.smf.jpg"

; Shortcuts
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateDirectory "$SMPROGRAMS\$ICONS_GROUP"
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\BattleHub.lnk" "$SYSDIR\javaw.exe" "-jar BattleHub.jar" "$INSTDIR\AFL.ico"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -AdditionalIcons
  SetOutPath $INSTDIR
  !insertmacro MUI_STARTMENU_WRITE_BEGIN Application
  CreateShortCut "$SMPROGRAMS\$ICONS_GROUP\Uninstall BattleHub.lnk" "$INSTDIR\uninst.exe"
  !insertmacro MUI_STARTMENU_WRITE_END
SectionEnd

Section -Post
  WriteUninstaller "$INSTDIR\uninst.exe"
  WriteRegStr HKLM "${PRODUCT_DIR_REGKEY}" "" "$INSTDIR\AFLobby.jar"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayName" "$(^Name)"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "UninstallString" "$INSTDIR\uninst.exe"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayIcon" "$INSTDIR\AFL.ico"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "DisplayVersion" "${PRODUCT_VERSION}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "URLInfoAbout" "${PRODUCT_WEB_SITE}"
  WriteRegStr ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}" "Publisher" "${PRODUCT_PUBLISHER}"
SectionEnd


Function un.onUninstSuccess
  HideWindow
  MessageBox MB_ICONINFORMATION|MB_OK "$(^Name) was successfully removed from your computer."
FunctionEnd

Function un.onInit
  MessageBox MB_ICONQUESTION|MB_YESNO|MB_DEFBUTTON2 "Are you sure you want to completely remove $(^Name) and all of its components?" IDYES +2
  Abort
FunctionEnd

Section Uninstall
  !insertmacro MUI_STARTMENU_GETFOLDER "Application" $ICONS_GROUP
  Delete "$INSTDIR\${PRODUCT_NAME}.url"
  Delete "$INSTDIR\uninst.exe"
  Delete "$INSTDIR\lobby\battlehub\sounds\question.wav"
  Delete "$INSTDIR\lobby\battlehub\images\UI\icon.png"
  Delete "$INSTDIR\lobby\battlehub\images\UI\alarm.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\yourock.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\yay.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\worship2.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\twisted.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\travolta.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\toothy4.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\toiletclaw.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\thumbup.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\stupid.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\stunned.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\spammer.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\soldier.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0094.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0085.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0082.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0073.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0049.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0036.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0033.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sign0030.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\sadmonk.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\rubberducky.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\roillingcat.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\redface.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\puke.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\pikachu.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\pikachu-head.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\offtopic.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\notfuneh.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\nixpenguin.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\lurk2.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\Kirby.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\imslow.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\hide.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\headbash.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\happy.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\hamtaro.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\gundam.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\freak.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\flour.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\fireworks.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\evilimu.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\evil.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\cry.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\crazy.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\cool.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\coffee.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\character0160.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\character0109.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\character0100.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\cesure.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\bty.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\brtfm.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\bowl.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\blies.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\beta.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\badger.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\asdf.gif"
  Delete "$INSTDIR\lobby\battlehub\images\smileys\afro.gif"
  Delete "$INSTDIR\lobby\battlehub\images\ranks\4.png"
  Delete "$INSTDIR\lobby\battlehub\images\ranks\3.png"
  Delete "$INSTDIR\lobby\battlehub\images\ranks\2.png"
  Delete "$INSTDIR\lobby\battlehub\images\ranks\1.png"
  Delete "$INSTDIR\lobby\battlehub\images\ranks\0.png"
  Delete "$INSTDIR\lobby\battlehub\images\icon.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ZW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ZM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ZA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\YU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\YT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\YE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\WS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\WF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\VA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\UZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\UY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\US.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\UM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\UG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\UA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TJ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\TC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ST.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SJ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SB.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\SA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\RW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\RU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\RO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\RE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\QA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\PA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\OM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\NA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MX.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MQ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ML.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\MA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LB.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\LA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\KE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\JP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\JO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\JM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IQ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\IE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ID.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\HK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GQ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GB.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\GA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FJ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\FI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ET.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ES.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\ER.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\EH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\EG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\EE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\EC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DJ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\DE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CX.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CK.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CC.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\CA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BY.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BV.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BJ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BH.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BD.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BB.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\BA.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AZ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AW.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AU.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AT.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AS.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AR.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AQ.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AP.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AO.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AN.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AM.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AL.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AI.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AG.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AF.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AE.png"
  Delete "$INSTDIR\lobby\battlehub\images\flags\AD.png"
  Delete "$INSTDIR\lib\substance.jar"
  Delete "$INSTDIR\lib\flamingo.jar"
  Delete "$INSTDIR\AFL.ico"
  Delete "$INSTDIR\AFLobbyStarter.exe"
  Delete "$INSTDIR\AFLobby.jar"
  Delete "$INSTDIR\BattleHub.jar"

  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall AFLobby.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\AFLobby.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\Uninstall BattleHub.lnk"
  Delete "$SMPROGRAMS\$ICONS_GROUP\BattleHub.lnk"

  RMDir "$SMPROGRAMS\$ICONS_GROUP"
  RMDir "$INSTDIR\lobby\battlehub\sounds"
  RMDir "$INSTDIR\lobby\battlehub\images\UI"
  RMDir "$INSTDIR\lobby\battlehub\images\smileys"
  RMDir "$INSTDIR\lobby\battlehub\images\ranks"
  RMDir "$INSTDIR\lobby\battlehub\images\flags"
  RMDir "$INSTDIR\lobby\battlehub\images"
  RMDir "$INSTDIR\lobby\battlehub\minimaps"
  RMDir "$INSTDIR\lib"
  RMDir "$INSTDIR"

  DeleteRegKey ${PRODUCT_UNINST_ROOT_KEY} "${PRODUCT_UNINST_KEY}"
  DeleteRegKey HKLM "${PRODUCT_DIR_REGKEY}"
  SetAutoClose true
SectionEnd