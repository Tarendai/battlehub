import lobbyscript
api = lobbyscript.Callback()

def out_SAY(chan, msg):
	if chan == 'code':
		exec msg