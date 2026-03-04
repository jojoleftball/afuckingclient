import zipfile, os, sys
path=os.path.expanduser(r'~\.gradle\caches\fabric-loom\minecraftMaven\net\minecraft\minecraft-merged\1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2\minecraft-merged-1.21.11-loom.mappings.1_21_11.layered+hash.2198-v2.jar')
print('checking', path)
if not os.path.exists(path):
    sys.exit('jar not found')
with zipfile.ZipFile(path) as z:
    for name in z.namelist():
        if name.endswith('Screen.class'):
            print(name)
