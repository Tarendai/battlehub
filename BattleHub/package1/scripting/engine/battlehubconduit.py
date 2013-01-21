from org.darkstars.battlehub.framework import CEvent
from org.darkstars.battlehub.framework import CEventTypes
from org.darkstars.battlehub.framework import IModule

class BattleHubConduit(IModule):
	
	def __init__(self):
		return
	
	def Init(self, l):
		self.lobby = l
	
	def Update(self):
		#
		return
	
	# traffic has been recieved
	# e.data is an array of strings
	# e.parameters is a string, but its basically every thing AFTER the first word but not including it
	# for example "SAID PRIVATE []AF moo" becomes "[]AF moo"
	# e.IsEvent("SAID") will match the first value in the message and return a boolean
	def NewEvent(self, e):
		handleIn(e.GetDataLine())
		return
	
	def NewGUIEvent(self, e):
		#
		return
	
	def OnRemove(self):
		# get rid of L
		lobby = None
		return
	
	def GetLobby(self):
		return self.lobby
	
	def equals(self, obj):
		if self == obj:
			return True
		return False
	
	# Analogous to the NewEvent message. This sends an Event object to the main message handler so that
	# it can be processed by the lobby note, this does not send data to the server, and the event fired will
	# eventually be passed to NewEvent so be careful not to start infinite loops
	#
	# This is mainly used for handling recieved traffic
	def FireEvent(self, e):
		return
	
	# Same as above but for GUI events
	def FireGUIEvent(self, e):
		self.lobby.GetCore().NewGUIEvent(e)
		return
	
	# Sends traffic to the end server
	def SendTraffic(self, trafficString):
		self.lobby.GetProtocol().SendTraffic(trafficString)
		return
		
	def SetScriptCallback(self, c):
		self.scriptCallback = c
		return
	
	def GetScriptCallback(self):
		return self.scriptCallback

callbackObject = None
		
def getBattleHubReturnObject():
	global callbackObject
	if callbackObject == None:
		callbackObject = BattleHubConduit()
	return callbackObject